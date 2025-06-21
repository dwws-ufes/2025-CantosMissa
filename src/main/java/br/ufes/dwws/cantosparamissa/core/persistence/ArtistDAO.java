package br.ufes.dwws.cantosparamissa.core.persistence;

import br.ufes.dwws.cantosparamissa.core.domain.Artist;
import br.ufes.inf.labes.jbutler.ejb.persistence.BaseDAO;
import br.ufes.inf.labes.jbutler.ejb.persistence.exceptions.MultiplePersistentObjectsFoundException;
import br.ufes.inf.labes.jbutler.ejb.persistence.exceptions.PersistentObjectNotFoundException;
import jakarta.ejb.Local;

import java.util.List;

@Local
public interface ArtistDAO extends BaseDAO<Artist> {
    public List<Artist> searchByName(String namePart);
    public Artist retrieveByFullName(String fullName)
            throws PersistentObjectNotFoundException, MultiplePersistentObjectsFoundException;
}
