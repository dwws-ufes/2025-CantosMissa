package br.ufes.dwws.cantosparamissa.core.application;

import br.ufes.dwws.cantosparamissa.core.application.exceptions.SystemAlreadyInstalledException;
import br.ufes.dwws.cantosparamissa.core.domain.Role;
import br.ufes.dwws.cantosparamissa.core.domain.User;
import br.ufes.dwws.cantosparamissa.core.persistence.UserDAO;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;

import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class ConfigureSystemServiceBean implements ConfigureSystemService {
    /** Logger for this class. */
    private static final Logger logger =
            Logger.getLogger(ConfigureSystemServiceBean.class.getCanonicalName());

    /** The Jakarta Security password hash generator. */
    @Inject
    private Pbkdf2PasswordHash passwordHash;

    /** The User DAO is used to create the admin user during installation. */
    @EJB
    private UserDAO userDAO;

    @Override
    public void installSystem() throws SystemAlreadyInstalledException {
        // Checks if the system has already been installed. Cannot be installed twice.
        if (userDAO.retrieveCount() > 0) {
            logger.log(Level.WARNING, "The system has already been installed. New installation aborted.");
            throw new SystemAlreadyInstalledException();
        }

        // Creates the administrator account with basic login information.
        logger.log(Level.FINE,
                "Creating basic administrator account with login 'admin' and password 'admin'.");
        String password = passwordHash.generate("admin".toCharArray());
        User admin = new User("admin", password, Role.ADMIN);
        userDAO.save(admin);
    }
}
