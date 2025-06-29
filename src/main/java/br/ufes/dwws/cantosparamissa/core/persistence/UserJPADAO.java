package br.ufes.dwws.cantosparamissa.core.persistence;

import br.ufes.dwws.cantosparamissa.core.domain.User;
import br.ufes.inf.labes.jbutler.ejb.persistence.BaseJPADAO;
import br.ufes.inf.labes.jbutler.ejb.persistence.exceptions.MultiplePersistentObjectsFoundException;
import br.ufes.inf.labes.jbutler.ejb.persistence.exceptions.PersistentObjectNotFoundException;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@Stateless
public class UserJPADAO extends BaseJPADAO<User> implements UserDAO {
  @PersistenceContext
  private EntityManager entityManager;

  @Override
  protected EntityManager getEntityManager() {
    return entityManager;
  }

  @Override
  public List<User> searchByEmail(String emailPart) {
    return entityManager.createQuery(
                    "SELECT u FROM User u WHERE LOWER(u.email) LIKE :email", User.class)
            .setParameter("email", "%" + emailPart.toLowerCase() + "%")
            .getResultList();
  }

  @Override
  public User retrieveByEmail(String email)
          throws PersistentObjectNotFoundException, MultiplePersistentObjectsFoundException {
    List<User> results = entityManager.createQuery(
                    "SELECT u FROM User u WHERE LOWER(u.email) = :email", User.class)
            .setParameter("email", email.toLowerCase())
            .getResultList();

    if (results.isEmpty()) {
      throw new PersistentObjectNotFoundException(null, User.class, email);
    } else if (results.size() > 1) {
      throw new MultiplePersistentObjectsFoundException(null, User.class, email);
    } else {
      return results.get(0);
    }
  }
}
