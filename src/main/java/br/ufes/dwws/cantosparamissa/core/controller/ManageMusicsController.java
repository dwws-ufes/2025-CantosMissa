package br.ufes.dwws.cantosparamissa.core.controller;

import br.ufes.dwws.cantosparamissa.core.application.ManageArtistsService;
import br.ufes.dwws.cantosparamissa.core.application.ManageMusicsService;
import br.ufes.dwws.cantosparamissa.core.domain.*;
import br.ufes.dwws.cantosparamissa.core.persistence.ArtistDAO;
import br.ufes.inf.labes.jbutler.ejb.application.CrudService;
import br.ufes.inf.labes.jbutler.ejb.controller.CrudController;
import br.ufes.inf.labes.jbutler.ejb.controller.PersistentObjectConverterFromId;
import jakarta.ejb.EJB;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
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

    // Conversor usado no campo de autocomplete do artista
    private PersistentObjectConverterFromId<Artist> artistConverter;
    @Inject
    void initConverter(ArtistDAO artistDAO) {
        artistConverter = new PersistentObjectConverterFromId<>(artistDAO);
    }
    public PersistentObjectConverterFromId<Artist> getArtistConverter() {
        return artistConverter;
    }
}
