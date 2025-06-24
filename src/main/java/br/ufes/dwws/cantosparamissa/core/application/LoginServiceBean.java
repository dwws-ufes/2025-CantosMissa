package br.ufes.dwws.cantosparamissa.core.application;

import br.ufes.dwws.cantosparamissa.core.application.exceptions.LoginFailedException;
import br.ufes.dwws.cantosparamissa.core.domain.User;
import br.ufes.dwws.cantosparamissa.core.persistence.UserDAO;
import br.ufes.inf.labes.jbutler.ejb.persistence.exceptions.MultiplePersistentObjectsFoundException;
import br.ufes.inf.labes.jbutler.ejb.persistence.exceptions.PersistentObjectNotFoundException;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBTransactionRolledbackException;
import jakarta.ejb.SessionContext;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Stateless session bean that implements the Login use case. See its local EJB interface for more
 * information.
 *
 */
@Stateless
@PermitAll
@Named
public class LoginServiceBean implements LoginService {
    /** Logger for this class. */
    private static final Logger logger = Logger.getLogger(LoginServiceBean.class.getCanonicalName());

    /** The Jakarta Security password hash generator. */
    @SuppressWarnings("CdiInjectionPointsInspection")
    @Inject
    private Pbkdf2PasswordHash passwordHash;

    /**
     * The User DAO is used to retrieve the user that is trying to log in to check their password.
     */
    @EJB
    private UserDAO userDAO;

    /** The EJB session context, used to retrieve the user that logged in via Jakarta Security. */
    @Resource
    private SessionContext sessionContext;

    @Override
    public void login(String username, String password) throws LoginFailedException {
        try {
            // Obtains the user given the e-mail address (that serves as username).
            logger.log(Level.FINER, "Authenticating user with username \"{0}\"...", username);
            User user = userDAO.retrieveByEmail(username);

            // Checks if the passwords match.
            if ((user.getPassword() != null) && passwordHash.verify(password.toCharArray(),
                    user.getPassword())) {
                logger.log(Level.FINEST, "Passwords match for user \"{0}\".", username);

                // Login successful. The retrieved user is the current user.
                logger.log(Level.FINE, "User \"{0}\" successfully logged in.", username);
            } else {
                // Passwords don't match.
                logger.log(Level.INFO, "User \"{0}\" not logged in: password didn't match.", username);
                throw new LoginFailedException(LoginFailedException.LoginFailedReason.INCORRECT_PASSWORD);
            }
        } catch (PersistentObjectNotFoundException e) {
            // No user was found with the given username.
            logger.log(Level.INFO,
                    "User \"{0}\" not logged in: no registered user found with given username.",
                    username);
            throw new LoginFailedException(e, LoginFailedException.LoginFailedReason.UNKNOWN_USERNAME);
        } catch (MultiplePersistentObjectsFoundException e) {
            // Multiple users were found with the same username.
            logger.log(Level.WARNING,
                    "User \"{0}\" not logged in: there are more than one registered user with the given username.",
                    username);
            throw new LoginFailedException(e, LoginFailedException.LoginFailedReason.MULTIPLE_USERS);
        } catch (EJBTransactionRolledbackException e) {
            // Unknown original cause. Throw the EJB exception.
            logger.log(Level.WARNING, "User \"" + username + "\" not logged in: unknown cause.", e);
            throw e;
        }
    }

    @Override
    public User getCurrentUser() {
        try {
            return userDAO.retrieveByEmail(sessionContext.getCallerPrincipal().getName());
        } catch (PersistentObjectNotFoundException | MultiplePersistentObjectsFoundException e) {
            return null;
        }
    }
}

