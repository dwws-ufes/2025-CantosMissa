package br.ufes.dwws.cantosparamissa.core.application;

import br.ufes.dwws.cantosparamissa.core.domain.Artist;
import br.ufes.inf.labes.jbutler.ejb.application.CrudService;
import jakarta.ejb.Local;

@Local
public interface RegistrationArtistService extends CrudService<Artist> {
}
