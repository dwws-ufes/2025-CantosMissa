package br.ufes.dwws.cantosparamissa.core.view;

import br.ufes.dwws.cantosparamissa.core.domain.Artist;
import br.ufes.dwws.cantosparamissa.core.persistence.ArtistDAO;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Named
@ViewScoped
public class ArtistView implements Serializable {
    private List<Artist> artists = List.of();
    private String query;

    @Inject
    private ArtistDAO artistDAO;

    @PostConstruct
    public void init() {
        Map<String, String> params = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getRequestParameterMap();

        String queryParam = params.get("q");
        if (queryParam != null && !queryParam.isBlank()) {
            query = queryParam;
            artists = artistDAO.searchByName(queryParam);
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

