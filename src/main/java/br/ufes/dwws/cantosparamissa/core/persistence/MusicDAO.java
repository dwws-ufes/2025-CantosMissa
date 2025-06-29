package br.ufes.dwws.cantosparamissa.core.persistence;

import br.ufes.dwws.cantosparamissa.core.domain.Artist;
import br.ufes.dwws.cantosparamissa.core.domain.LiturgicalSeason;
import br.ufes.dwws.cantosparamissa.core.domain.Music;
import br.ufes.dwws.cantosparamissa.core.domain.SongType;
import br.ufes.inf.labes.jbutler.ejb.persistence.BaseDAO;
import jakarta.ejb.Local;

import java.util.List;

@Local
public interface MusicDAO extends BaseDAO<Music> {
    public List<Music> searchByTitle(String title);
    public List<Music> searchByLiturgicalSeason(LiturgicalSeason liturgicalSeason);
    public List<Music> searchByLiturgicalSeasonAndSongType(LiturgicalSeason liturgicalSeason, SongType songType);
    public List<Music> searchByArtist(String name);
}
