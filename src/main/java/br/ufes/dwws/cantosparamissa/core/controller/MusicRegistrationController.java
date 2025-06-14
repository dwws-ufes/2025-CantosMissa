package br.ufes.dwws.cantosparamissa.core.controller;

import br.ufes.dwws.cantosparamissa.core.application.RegistrationMusicService;
import br.ufes.dwws.cantosparamissa.core.domain.Music;
import br.ufes.inf.labes.jbutler.ejb.application.CrudService;
import br.ufes.inf.labes.jbutler.ejb.controller.CrudController;
import jakarta.ejb.EJB;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named
@ViewScoped
public class MusicRegistrationController extends CrudController<Music> {
    @EJB
    private RegistrationMusicService registrationMusicService;

    @Override
    protected CrudService<Music> getCrudService() {
        return registrationMusicService;
    }
}
