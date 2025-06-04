package br.ufes.dwws.cantosparamissa.core.application;

import br.ufes.dwws.cantosparamissa.core.application.exceptions.LoginFailedException;
import br.ufes.dwws.cantosparamissa.core.domain.User;
import jakarta.ejb.Local;

import java.io.Serializable;

/**
 * Local EJB interface for the service that implements authentication (login), allowing any user
 * to authenticate herself and log in the system.
 * <p>
 * This service also provides the logged-in user to other beans that may need it.
 *
 */
@Local
public interface LoginService extends Serializable {
    /**
     * Authenticates a user given their username and password. If the user is correctly authenticated,
     * they should be available as an Academic object through the getCurrentUser() method.
     *
     * @param username The username that identifies the user in the system.
     * @param password The password that authenticates the user.
     * @throws LoginFailedException If the username is unknown or the password is incorrect.
     */
    void login(String username, String password) throws LoginFailedException;

    /**
     * Obtains the currently logged-in user.
     *
     * @return The User object that represents the user that is currently logged in.
     */
    User getCurrentUser();
}

