package br.ufes.dwws.cantosparamissa.core.application;

import br.ufes.dwws.cantosparamissa.core.domain.Artist;
import br.ufes.dwws.cantosparamissa.core.persistence.ArtistDAO;
import br.ufes.inf.labes.jbutler.ejb.application.CrudServiceImpl;
import br.ufes.inf.labes.jbutler.ejb.application.validation.CrudException;
import br.ufes.inf.labes.jbutler.ejb.persistence.BaseDAO;
import br.ufes.inf.labes.jbutler.ejb.persistence.exceptions.MultiplePersistentObjectsFoundException;
import br.ufes.inf.labes.jbutler.ejb.persistence.exceptions.PersistentObjectNotFoundException;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
@PermitAll
public class ManageArtistsServiceBean extends CrudServiceImpl<Artist> implements ManageArtistsService{
    @EJB
    private ArtistDAO artistDAO;

    @Override
    public BaseDAO<Artist> getDAO() {
        return artistDAO;
    }

    @Override
    public List<Artist> findByNameContaining(String namePart) {
        return artistDAO.searchByName(namePart);
    }

    @Override
    public void validateCreate(Artist entity) throws CrudException {
        CrudException crudException = null;
        String exceptionMessage = "O artista não pode ser criado devido a erros de validação.";

        // Regra de validação 1: não podem existir dois artistas com mesmo nome
        boolean violated = true;
        try {
            artistDAO.retrieveByFullName(entity.getName());
        } catch (PersistentObjectNotFoundException e) {
            // Tudo certo
            violated = false;
        } catch (MultiplePersistentObjectsFoundException e) {
            Logger.getLogger(ManageArtistsServiceBean.class.getName()).log(
                    Level.WARNING,
                    String.format(
                            "Foram encontrados múltiplos registros para a entidade %s com o parâmetro: %s",
                            e.getEntityClass().getSimpleName(),
                            e.getParams() != null && e.getParams().length > 0 ? e.getParams()[0] : "N/A"
                    ),
                    e
            );
        }

        // Se regra 1 violada, adiciona à exceção de CRUD
        if (violated)
            crudException = addFieldValidationError(crudException, exceptionMessage,
                    "nameField", "manageArtists.error.nameExists");

        // Regra de validação 2: imagens não podem ter mais de 1 MB
        byte[] picture = entity.getPicture();
        if (picture != null) {
            if (picture.length > 1048576) {
                crudException = addFieldValidationError(crudException, exceptionMessage,
                        "pictureField", "manageArtists.error.fileTooLarge");
            }
        }

        // Se alguma regra foi violada, lança a exceção
        if (crudException != null)
            throw crudException;
    }

    @Override
    public void validateUpdate(Artist entity) throws CrudException {
        CrudException crudException = null;
        String exceptionMessage = "O artista não pode ser editado devido a erros de validação.";

        // Regra de validação = não podem existir dois artistas com mesmo nome
        boolean violated = false;
        try {
            Artist existing = artistDAO.retrieveByFullName(entity.getName());
            if (!entity.equals(existing)) {
                violated = true;
            }
        } catch (PersistentObjectNotFoundException e) {
            // Tudo certo
        } catch (MultiplePersistentObjectsFoundException e) {
            violated = true;
            Logger.getLogger(ManageArtistsServiceBean.class.getName()).log(
                    Level.WARNING,
                    String.format(
                            "Foram encontrados múltiplos registros para a entidade %s com o parâmetro: %s",
                            e.getEntityClass().getSimpleName(),
                            e.getParams() != null && e.getParams().length > 0 ? e.getParams()[0] : "N/A"
                    ),
                    e
            );
        }

        // Se regra 1 violada, adiciona à exceção de CRUD
        if (violated)
            crudException = addFieldValidationError(crudException, exceptionMessage,
                    "nameField", "manageArtists.error.nameExists");

        // Regra de validação 2: imagens não podem ter mais de 1 MB
        byte[] picture = entity.getPicture();
        if (picture != null) {
            if (picture.length > 1048576) {
                crudException = addFieldValidationError(crudException, exceptionMessage,
                        "pictureField", "manageArtists.error.fileTooLarge");
            }
        }

        // Se alguma regra foi violada, lança a exceção
        if (crudException != null)
            throw crudException;
    }
}
