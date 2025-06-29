package br.ufes.dwws.cantosparamissa.core.application;

import br.ufes.dwws.cantosparamissa.core.domain.User;
import br.ufes.dwws.cantosparamissa.core.persistence.UserDAO;
import br.ufes.inf.labes.jbutler.ejb.application.CrudServiceImpl;
import br.ufes.inf.labes.jbutler.ejb.application.validation.CrudException;
import br.ufes.inf.labes.jbutler.ejb.persistence.BaseDAO;
import br.ufes.inf.labes.jbutler.ejb.persistence.exceptions.MultiplePersistentObjectsFoundException;
import br.ufes.inf.labes.jbutler.ejb.persistence.exceptions.PersistentObjectNotFoundException;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
@RolesAllowed("ADMIN")
public class ManageUserServiceBean extends CrudServiceImpl<User> implements ManageUserService {
    @EJB
    private UserDAO userDAO;

    @Override
    public BaseDAO<User> getDAO() {
        return userDAO;
    }

    public List<User> findByEmailContaining(String emailPart) {
        return userDAO.searchByEmail(emailPart);
    }

    @Override
    public void validateCreate(User entity) throws CrudException {
        CrudException crudException = null;
        String exceptionMessage = "O usuário não pode ser criado devido a erros de validação.";

        // Regra 1: não podem existir dois usuários com mesmo email
        boolean violated = true;
        try {
            userDAO.retrieveByEmail(entity.getEmail());
        } catch (PersistentObjectNotFoundException e) {
            // Tudo certo
            violated = false;
        } catch (MultiplePersistentObjectsFoundException e) {
            Logger.getLogger(ManageUserServiceBean.class.getName()).log(
                    Level.WARNING,
                    String.format(
                            "Foram encontrados múltiplos registros para a entidade %s com o parâmetro: %s",
                            e.getEntityClass().getSimpleName(),
                            e.getParams() != null && e.getParams().length > 0 ? e.getParams()[0] : "N/A"
                    ),
                    e
            );
        }

        if (violated)
            crudException = addFieldValidationError(crudException, exceptionMessage,
                    "emailField", "manageUsers.error.emailExists");

        // Regra 2: senha não pode ser vazia e deve ter tamanho máximo, por exemplo 128
        if (entity.getPassword() == null || entity.getPassword().isBlank() || entity.getPassword().length() > 128) {
            crudException = addFieldValidationError(crudException, exceptionMessage,
                    "passwordField", "manageUsers.error.invalidPassword");
        }

        if (crudException != null)
            throw crudException;
    }

    @Override
    public void validateUpdate(User entity) throws CrudException {
        CrudException crudException = null;
        String exceptionMessage = "O usuário não pode ser editado devido a erros de validação.";

        // Regra: não podem existir dois usuários com mesmo email diferentes
        boolean violated = false;
        try {
            User existing = userDAO.retrieveByEmail(entity.getEmail());
            if (!entity.equals(existing)) {
                violated = true;
            }
        } catch (PersistentObjectNotFoundException e) {
            // Tudo certo
        } catch (MultiplePersistentObjectsFoundException e) {
            violated = true;
            Logger.getLogger(ManageUserServiceBean.class.getName()).log(
                    Level.WARNING,
                    String.format(
                            "Foram encontrados múltiplos registros para a entidade %s com o parâmetro: %s",
                            e.getEntityClass().getSimpleName(),
                            e.getParams() != null && e.getParams().length > 0 ? e.getParams()[0] : "N/A"
                    ),
                    e
            );
        }

        if (violated)
            crudException = addFieldValidationError(crudException, exceptionMessage,
                    "emailField", "manageUsers.error.emailExists");

        // Validação senha similar à create
        if (entity.getPassword() == null || entity.getPassword().isBlank() || entity.getPassword().length() > 128) {
            crudException = addFieldValidationError(crudException, exceptionMessage,
                    "passwordField", "manageUsers.error.invalidPassword");
        }

        if (crudException != null)
            throw crudException;
    }


    public void createUser(User user, String password) {
        // Aqui você deve codificar a senha antes de salvar
        user.setPassword(encryptPassword(password));
        userDAO.save(user);
    }

    @Override
    public void updatePassword(User user, String newPassword) {
        user.setPassword(encryptPassword(newPassword));
        userDAO.save(user); // ou update(user)
    }

    @Override
    public List<User> findAll() {
        return userDAO.retrieveAll();
    }

    private String encryptPassword(String password) {
        try {
            int iterations = 2048;
            int keyLength = 256;
            String algorithm = "PBKDF2WithHmacSHA256";

            byte[] salt = generateSalt();
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, keyLength);
            SecretKeyFactory skf = SecretKeyFactory.getInstance(algorithm);
            byte[] hash = skf.generateSecret(spec).getEncoded();

            return String.format("%s:%d:%s:%s",
                    algorithm,
                    iterations,
                    Base64.getEncoder().encodeToString(salt),
                    Base64.getEncoder().encodeToString(hash));
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criptografar senha", e);
        }
    }

    private byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

}
