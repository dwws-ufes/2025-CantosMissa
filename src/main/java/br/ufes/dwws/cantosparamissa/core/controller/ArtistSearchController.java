package br.ufes.dwws.cantosparamissa.core.controller;

import br.ufes.dwws.cantosparamissa.core.application.ArtistSearchService;
import br.ufes.dwws.cantosparamissa.core.domain.Artist;
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
public class ArtistSearchController implements Serializable {
    private List<Artist> artists = List.of();
    private String query;

    @EJB
    private ArtistSearchService artistSearchService;

    @PostConstruct
    public void init() {
        Map<String, String> params = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getRequestParameterMap();

        String queryParam = params.get("q");
        if (queryParam != null && !queryParam.isBlank()) {
            query = queryParam;
            artists = artistSearchService.searchByName(queryParam);
        }
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}

