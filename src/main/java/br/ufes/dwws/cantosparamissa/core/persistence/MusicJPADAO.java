package br.ufes.dwws.cantosparamissa.core.persistence;

import br.ufes.dwws.cantosparamissa.core.domain.Music;
import br.ufes.inf.labes.jbutler.ejb.persistence.BaseJPADAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public class MusicJPADAO extends BaseJPADAO<Music> implements MusicDAO{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    protected EntityManager getEntityManager() {
        return null;
    }
}
