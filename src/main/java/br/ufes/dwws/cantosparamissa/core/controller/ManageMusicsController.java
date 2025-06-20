package br.ufes.dwws.cantosparamissa.core.controller;

import br.ufes.dwws.cantosparamissa.core.application.ManageArtistsService;
import br.ufes.dwws.cantosparamissa.core.application.ManageMusicsService;
import br.ufes.dwws.cantosparamissa.core.domain.*;
import br.ufes.inf.labes.jbutler.ejb.application.CrudService;
import br.ufes.inf.labes.jbutler.ejb.controller.CrudController;
import jakarta.ejb.EJB;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.util.List;

@Named
@ViewScoped
public class ManageMusicsController extends CrudController<Music> {
    @EJB
    private ManageMusicsService manageMusicsService;

    @EJB
    private ManageArtistsService manageArtistsService;

    private String artistName;

    @Override
    protected CrudService<Music> getCrudService() {
        return manageMusicsService;
    }

    public MusicKey[] getMusicKeyValues() {
        return MusicKey.values();
    }

    public LiturgicalSeason[] getLiturgicalSeasonValues() {
        return LiturgicalSeason.values();
    }

    public SongType[] getSongTypeValues() {
        return SongType.values();
    }

    public List<Artist> completeArtist(String query) {
        return manageArtistsService.findByNameContaining(query);
    }

    public String getArtistName() { return artistName; }

    public void setArtistName(String artistName) { this.artistName = artistName; }

    protected void prepEntity() {
        if (artistName != null && !artistName.trim().isEmpty()) {
            // Procura o artista pelo nome
            List<Artist> matches = manageArtistsService.findByNameContaining(artistName);
            if (!matches.isEmpty()) {
                selectedEntity.setArtist(matches.getFirst()); // usa o primeiro correspondente
            } else {
                // Cria novo artista
                Artist newArtist = new Artist();
                newArtist.setName(artistName);
                manageArtistsService.getDAO().save(newArtist);
                selectedEntity.setArtist(newArtist);
            }
        }
    }
}
