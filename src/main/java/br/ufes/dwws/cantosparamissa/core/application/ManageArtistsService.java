package br.ufes.dwws.cantosparamissa.core.application;

import br.ufes.dwws.cantosparamissa.core.domain.Artist;
import br.ufes.inf.labes.jbutler.ejb.application.CrudService;
import br.ufes.inf.labes.jbutler.ejb.persistence.exceptions.MultiplePersistentObjectsFoundException;
import br.ufes.inf.labes.jbutler.ejb.persistence.exceptions.PersistentObjectNotFoundException;

import java.util.List;

public interface ManageArtistsService extends CrudService<Artist> {
    List<Artist> findByNameContaining(String namePart);
    Artist retrieveByName(String name) throws PersistentObjectNotFoundException, MultiplePersistentObjectsFoundException;
}
