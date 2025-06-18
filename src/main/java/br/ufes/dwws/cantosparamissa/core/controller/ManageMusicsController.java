package br.ufes.dwws.cantosparamissa.core.controller;

import br.ufes.dwws.cantosparamissa.core.application.ManageMusicsService;
import br.ufes.dwws.cantosparamissa.core.domain.LiturgicalSeason;
import br.ufes.dwws.cantosparamissa.core.domain.Music;
import br.ufes.dwws.cantosparamissa.core.domain.MusicKey;
import br.ufes.dwws.cantosparamissa.core.domain.SongType;
import br.ufes.inf.labes.jbutler.ejb.application.CrudService;
import br.ufes.inf.labes.jbutler.ejb.controller.CrudController;
import jakarta.ejb.EJB;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named
@ViewScoped
public class ManageMusicsController extends CrudController<Music> {
    @EJB
    private ManageMusicsService manageMusicsService;

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
}
