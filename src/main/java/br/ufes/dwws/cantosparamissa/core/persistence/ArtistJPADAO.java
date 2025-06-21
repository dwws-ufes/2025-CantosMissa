package br.ufes.dwws.cantosparamissa.core.persistence;

import br.ufes.dwws.cantosparamissa.core.domain.Artist;
import br.ufes.inf.labes.jbutler.ejb.persistence.BaseJPADAO;
import br.ufes.inf.labes.jbutler.ejb.persistence.exceptions.MultiplePersistentObjectsFoundException;
import br.ufes.inf.labes.jbutler.ejb.persistence.exceptions.PersistentObjectNotFoundException;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@Stateless
public class ArtistJPADAO extends BaseJPADAO<Artist> implements ArtistDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    public List<Artist> searchByName(String namePart) {
        return entityManager.createQuery("SELECT a FROM Artist a WHERE LOWER(a.name) LIKE :name", Artist.class)
                .setParameter("name", "%" + namePart.toLowerCase() + "%")
                .getResultList();
    }

    public Artist retrieveByFullName(String fullName)
            throws PersistentObjectNotFoundException, MultiplePersistentObjectsFoundException {
        List<Artist> results = entityManager.createQuery(
                        "SELECT a FROM Artist a WHERE LOWER(a.name) = :name", Artist.class)
                .setParameter("name", fullName.toLowerCase())
                .getResultList();

        if (results.isEmpty()) {
            throw new PersistentObjectNotFoundException(null, Artist.class, fullName);
        } else if (results.size() > 1) {
            throw new MultiplePersistentObjectsFoundException(null, Artist.class, fullName);
        } else {
            return results.getFirst();
        }
    }
}
