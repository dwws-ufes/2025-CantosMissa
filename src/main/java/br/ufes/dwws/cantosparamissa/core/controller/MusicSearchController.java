package br.ufes.dwws.cantosparamissa.core.controller;

import br.ufes.dwws.cantosparamissa.core.application.MusicSearchService;
import br.ufes.dwws.cantosparamissa.core.domain.LiturgicalSeason;
import br.ufes.dwws.cantosparamissa.core.domain.Music;
import br.ufes.dwws.cantosparamissa.core.domain.SongType;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Named
@ViewScoped
public class MusicSearchController implements Serializable {
    private List<Music> musics = List.of();
    private String query;

    @EJB
    private MusicSearchService musicSearchService;

    @PostConstruct
    public void init() {
        Map<String, String> params = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getRequestParameterMap();

        String seasonParam = params.get("season");
        String typeParam = params.get("type");
        String queryParam = params.get("q");

        if (seasonParam != null && typeParam != null) {
            try {
                var season = LiturgicalSeason.valueOf(seasonParam);
                var type = SongType.valueOf(typeParam);
                musics = musicSearchService.searchByLiturgicalSeasonAndSongType(season, type);
                return;
            } catch (IllegalArgumentException e) {
                musics = List.of();
                return;
            }
        }

        if (seasonParam != null) {
            try {
                var season = LiturgicalSeason.valueOf(seasonParam);
                musics = musicSearchService.searchByLiturgicalSeason(season);
                return;
            } catch (IllegalArgumentException e) {
                musics = List.of();
                return;
            }
        }

        if (queryParam != null && !queryParam.isBlank()) {
            query = queryParam;
            musics = musicSearchService.searchByTitle(queryParam);
        }
    }

    public List<Music> getMusics() {
        return musics;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}