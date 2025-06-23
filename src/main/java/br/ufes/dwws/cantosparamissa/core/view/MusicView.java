package br.ufes.dwws.cantosparamissa.core.view;

import br.ufes.dwws.cantosparamissa.core.domain.LiturgicalSeason;
import br.ufes.dwws.cantosparamissa.core.domain.Music;
import br.ufes.dwws.cantosparamissa.core.domain.SongType;
import br.ufes.dwws.cantosparamissa.core.persistence.MusicDAO;
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
public class MusicView implements Serializable {

    private List<Music> musics;

    @Inject
    private MusicDAO musicDAO;

    @PostConstruct
    public void init() {
        Map<String, String> params = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getRequestParameterMap();

        String seasonParam = params.get("season");
        String typeParam = params.get("type");

        if (seasonParam != null && typeParam != null) {
            try {
                LiturgicalSeason liturgicalSeason = LiturgicalSeason.valueOf(seasonParam);
                SongType songType = SongType.valueOf(typeParam);
                musics = musicDAO.searchByLiturgicalSeasonAndSongType(liturgicalSeason, songType);
            } catch (IllegalArgumentException e) {
                musics = List.of(); // parâmetros inválidos
            }
        } else if (seasonParam != null) {
            try {
                LiturgicalSeason liturgicalSeason = LiturgicalSeason.valueOf(seasonParam);
                musics = musicDAO.searchByLiturgicalSeason(liturgicalSeason);
            } catch (IllegalArgumentException e) {
                musics = List.of();
            }
        } else {
            musics = List.of(); // nenhum parâmetro
        }
    }

    public List<Music> getMusics() {
        return musics;
    }
}