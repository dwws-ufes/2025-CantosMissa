package br.ufes.dwws.cantosparamissa.core.persistence;

import br.ufes.dwws.cantosparamissa.core.domain.Artist;
import br.ufes.dwws.cantosparamissa.core.domain.User;
import br.ufes.inf.labes.jbutler.ejb.persistence.BaseDAO;
import br.ufes.inf.labes.jbutler.ejb.persistence.exceptions.MultiplePersistentObjectsFoundException;
import br.ufes.inf.labes.jbutler.ejb.persistence.exceptions.PersistentObjectNotFoundException;
import jakarta.ejb.Local;

import java.util.List;

@Local
public interface UserDAO extends BaseDAO<User> {
  public List<User> searchByEmail(String emailPart);
  User retrieveByEmail(String email)
      throws PersistentObjectNotFoundException, MultiplePersistentObjectsFoundException;
}