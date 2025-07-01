package br.ufes.dwws.cantosparamissa.core.application;

import br.ufes.dwws.cantosparamissa.core.domain.LiturgicalSeason;
import br.ufes.dwws.cantosparamissa.core.domain.Music;
import br.ufes.dwws.cantosparamissa.core.domain.SongType;
import jakarta.ejb.Local;

import java.util.List;

@Local
public interface MusicSearchService {
    List<Music> searchByLiturgicalSeasonAndSongType(LiturgicalSeason season, SongType songType);
    List<Music> searchByLiturgicalSeason(LiturgicalSeason season);
    List<Music> searchByTitle(String title);
}
