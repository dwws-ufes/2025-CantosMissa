package br.ufes.dwws.cantosparamissa.core.persistence;

import br.ufes.dwws.cantosparamissa.core.domain.Artist;
import br.ufes.dwws.cantosparamissa.core.domain.LiturgicalSeason;
import br.ufes.dwws.cantosparamissa.core.domain.Music;
import br.ufes.dwws.cantosparamissa.core.domain.SongType;
import br.ufes.inf.labes.jbutler.ejb.persistence.BaseJPADAO;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@Stateless
public class MusicJPADAO extends BaseJPADAO<Music> implements MusicDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    public List<Music> searchByTitle(String title) {
        return entityManager.createQuery("SELECT m FROM Music m WHERE m.title LIKE :title", Music.class)
                .setParameter("title", "%" + title + "%")
                .getResultList();
    }

    public List<Music> searchByLiturgicalSeason(LiturgicalSeason liturgicalSeason) {
        return entityManager.createQuery("SELECT m FROM Music m WHERE m.liturgicalSeason = :liturgicalSeason", Music.class)
                .setParameter("liturgicalSeason", liturgicalSeason)
                .getResultList();
    }

    public List<Music> searchByLiturgicalSeasonAndSongType(LiturgicalSeason liturgicalSeason, SongType songType) {
        return entityManager.createQuery("SELECT m FROM Music m WHERE m.liturgicalSeason = :liturgicalSeason AND m.songType = :songType", Music.class)
                .setParameter("liturgicalSeason", liturgicalSeason)
                .setParameter("songType", songType)
                .getResultList();
    }

    public List<Music> searchByArtist(Artist artist) {
        return entityManager.createQuery("SELECT m FROM Music m WHERE m.artist = :artist", Music.class)
                .setParameter("artist", artist)
                .getResultList();
    }
}
