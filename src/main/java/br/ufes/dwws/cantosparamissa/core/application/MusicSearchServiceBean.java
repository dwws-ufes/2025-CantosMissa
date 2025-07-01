package br.ufes.dwws.cantosparamissa.core.application;

import br.ufes.dwws.cantosparamissa.core.domain.LiturgicalSeason;
import br.ufes.dwws.cantosparamissa.core.domain.Music;
import br.ufes.dwws.cantosparamissa.core.domain.SongType;
import br.ufes.dwws.cantosparamissa.core.persistence.MusicDAO;
import jakarta.annotation.security.PermitAll;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import java.util.List;

@Stateless
@PermitAll
public class MusicSearchServiceBean implements  MusicSearchService {
    @EJB
    private MusicDAO musicDAO;

    @Override
    public List<Music> searchByLiturgicalSeasonAndSongType(LiturgicalSeason season, SongType songType) {
        return musicDAO.searchByLiturgicalSeasonAndSongType(season, songType);
    }

    @Override
    public List<Music> searchByLiturgicalSeason(LiturgicalSeason season) {
        return musicDAO.searchByLiturgicalSeason(season);
    }

    @Override
    public List<Music> searchByTitle(String title) {
        return musicDAO.searchByTitle(title);
    }
}
