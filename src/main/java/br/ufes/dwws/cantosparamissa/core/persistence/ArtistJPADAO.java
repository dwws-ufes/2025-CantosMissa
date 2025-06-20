package br.ufes.dwws.cantosparamissa.core.persistence;

import br.ufes.dwws.cantosparamissa.core.domain.Artist;
import br.ufes.inf.labes.jbutler.ejb.persistence.BaseJPADAO;
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
}
