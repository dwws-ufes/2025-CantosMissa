package br.ufes.dwws.cantosparamissa.core.controller;

import br.ufes.dwws.cantosparamissa.core.domain.Artist;
import br.ufes.dwws.cantosparamissa.core.domain.Music;
import br.ufes.dwws.cantosparamissa.core.persistence.ArtistDAO;
import br.ufes.dwws.cantosparamissa.core.persistence.MusicDAO;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.primefaces.event.SelectEvent;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Named
@ViewScoped
public class SearchController implements Serializable {
    private String query;
    private String type = "music"; // ou "artist"

    @Inject
    private MusicDAO musicDAO;

    @Inject
    private ArtistDAO artistDAO;


    // Busca completa, redireciona para a pagina de resultados da busca
    public String search() {
        if ("artist".equals(this.type)) {
            return "/core/artists/index.xhtml?q=" + query + "&faces-redirect=true";
        }
        return "/core/musics/index.xhtml?q=" + query + "&faces-redirect=true";
    }

    // Metodo para autocomplete
    public List<String> completeText(String query) {
        List<String> suggestions = new ArrayList<>();

        if (query == null || query.trim().length() < 2) {
            return suggestions;
        }

        try {
            if ("music".equals(type)) {
                List<Music> musics = musicDAO.searchByTitle(query.trim());
                if (musics.isEmpty()) {
                    suggestions.add("Nenhuma música encontrada");
                } else {
                    suggestions = musics.stream()
                            .map(Music::getTitle)
                            .limit(10)
                            .collect(Collectors.toList());
                }
            } else if ("artist".equals(this.type)) {
                List<Artist> artists = artistDAO.searchByName(query.trim());
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

    // Metodo para limpar a busca quando o tipo muda
    public void onTypeChange(AjaxBehaviorEvent event) {
        this.query = null; // Limpa a query quando muda o tipo
        System.out.println("Type changed to: " + this.type); // Debug
    }

    // Metodo para lidar com a seleção de um item
    public void onItemSelect(SelectEvent<String> event) {
        String selectedItem = event.getObject();
        if (selectedItem != null && !selectedItem.isEmpty()) {
            try {
                FacesContext context = FacesContext.getCurrentInstance();
                String redirectUrl = "";

                if ("music".equals(this.type)) {
                    // Busca a música pelo título para pegar o ID
                    List<Music> musics = musicDAO.searchByTitle(selectedItem);
                    if (!musics.isEmpty()) {
                        Music music = musics.get(0);
                        redirectUrl = context.getExternalContext().getRequestContextPath() +
                                "/core/musics/view.xhtml?id=" + music.getId();
                    }
                } else if ("artist".equals(this.type)) {
                    // Busca o artista pelo nome para pegar o ID
                    List<Artist> artists = artistDAO.searchByName(selectedItem);
                    if (!artists.isEmpty()) {
                        Artist artist = artists.get(0);
                        redirectUrl = context.getExternalContext().getRequestContextPath() +
                                "/core/artists/view.xhtml?id=" + artist.getId();
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
