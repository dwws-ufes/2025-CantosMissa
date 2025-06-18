package br.ufes.dwws.cantosparamissa.core.controller;

import br.ufes.dwws.cantosparamissa.core.application.RegistrationArtistService;
import br.ufes.dwws.cantosparamissa.core.domain.Artist;
import br.ufes.inf.labes.jbutler.ejb.application.CrudService;
import br.ufes.inf.labes.jbutler.ejb.controller.CrudController;
import jakarta.ejb.EJB;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named
@ViewScoped
public class ArtistRegistrationController extends CrudController<Artist> {
    @EJB
    private RegistrationArtistService registrationArtistService;

    @Override
    protected CrudService<Artist> getCrudService() {
        return registrationArtistService;
    }
}
