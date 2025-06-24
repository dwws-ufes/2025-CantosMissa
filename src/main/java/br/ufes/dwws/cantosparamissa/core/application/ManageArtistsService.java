package br.ufes.dwws.cantosparamissa.core.application;

import br.ufes.dwws.cantosparamissa.core.domain.Artist;
import br.ufes.inf.labes.jbutler.ejb.application.CrudService;

import java.util.List;

public interface ManageArtistsService extends CrudService<Artist> {
    List<Artist> findByNameContaining(String namePart);
}
