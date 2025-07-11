package br.ufes.dwws.cantosparamissa.core.controller;

import br.ufes.dwws.cantosparamissa.core.application.SearchService;
import br.ufes.dwws.cantosparamissa.core.domain.Artist;
import br.ufes.dwws.cantosparamissa.core.domain.Music;
import jakarta.annotation.security.PermitAll;
import jakarta.ejb.EJB;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import org.primefaces.event.SelectEvent;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Named
@ViewScoped
@PermitAll
public class SearchController implements Serializable {
    private String query;
    private String type = "music";

    @EJB
    private SearchService searchService;

    public String search() {
        if ("artist".equals(this.type)) {
            return "/public/artists/index.xhtml?q=" + query + "&faces-redirect=true";
        }
        return "/public/musics/index.xhtml?q=" + query + "&faces-redirect=true";
    }

    public List<String> completeText(String query) {
        List<String> suggestions = new ArrayList<>();

        if (query == null || query.trim().length() < 2) {
            return suggestions;
        }

        try {
            if ("music".equals(type)) {
                List<Music> musics = searchService.searchByTitle(query.trim());
                if (musics.isEmpty()) {
                    suggestions.add("Nenhuma música encontrada");
                } else {
                    suggestions = musics.stream()
                            .map(Music::getTitle)
                            .limit(10)
                            .collect(Collectors.toList());
                }
            } else if ("artist".equals(this.type)) {
                List<Artist> artists = searchService.searchByName(query.trim());
                if (artists.isEmpty()) {
                    suggestions.add("Nenhum artista encontrado");
                } else {
                    suggestions = artists.stream()
                            .map(Artist::getName)
                            .limit(10)
                            .collect(Collectors.toList());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            suggestions.add("Erro ao buscar sugestões");
        }

        return suggestions;
    }

    // Method to clear search when type changes
    public void onTypeChange(AjaxBehaviorEvent event) {
        this.query = null;
    }

    // Method for handling item selection
    public void onItemSelect(SelectEvent<String> event) {
        String selectedItem = event.getObject();
        if (selectedItem != null && !selectedItem.isEmpty()) {
            try {
                FacesContext context = FacesContext.getCurrentInstance();
                String redirectUrl = "";

                if ("music".equals(this.type)) {
                    // Search the song by title to get the ID
                    List<Music> musics = searchService.searchByTitle(selectedItem);
                    if (!musics.isEmpty()) {
                        Music music = musics.get(0);
                        redirectUrl = context.getExternalContext().getRequestContextPath() +
                                "/public/viewMusic/index.xhtml?id=" + music.getId();
                    }
                } else if ("artist".equals(this.type)) {
                    // Search for the artist by name to get the ID
                    List<Artist> artists = searchService.searchByName(selectedItem);
                    if (!artists.isEmpty()) {
                        Artist artist = artists.get(0);
                        redirectUrl = context.getExternalContext().getRequestContextPath() +
                                "/public/viewArtist/index.xhtml?id=" + artist.getId();
                    }
                }

                if (!redirectUrl.isEmpty()) {
                    context.getExternalContext().redirect(redirectUrl);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
