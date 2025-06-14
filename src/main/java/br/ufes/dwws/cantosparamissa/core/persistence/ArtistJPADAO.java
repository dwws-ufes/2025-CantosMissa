package br.ufes.dwws.cantosparamissa.core.persistence;

import br.ufes.dwws.cantosparamissa.core.domain.Artist;
import br.ufes.inf.labes.jbutler.ejb.persistence.BaseJPADAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public class ArtistJPADAO extends BaseJPADAO<Artist> implements ArtistDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }
}
