package br.ufes.dwws.cantosparamissa.core.controller;

import br.ufes.dwws.cantosparamissa.core.application.LoginService;
import br.ufes.dwws.cantosparamissa.core.application.exceptions.LoginFailedException;
import br.ufes.dwws.cantosparamissa.core.domain.Role;
import br.ufes.dwws.cantosparamissa.core.domain.User;
import br.ufes.inf.labes.jbutler.ejb.controller.JSFController;
import com.github.adminfaces.template.session.AdminSession;
import jakarta.annotation.security.PermitAll;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.enterprise.inject.Specializes;
import jakarta.faces.annotation.FacesConfig;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.security.enterprise.AuthenticationStatus;
import jakarta.security.enterprise.SecurityContext;
import jakarta.security.enterprise.authentication.mechanism.http.AuthenticationParameters;
import jakarta.security.enterprise.credential.Credential;
import jakarta.security.enterprise.credential.Password;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.omnifaces.util.Faces;

import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Session-scoped managed bean that provides to Web pages the login service, indication if the user
 * is logged in and, if so, information on the currently logged-in user.
 * <p>
 * This class integrates with the AdminFaces JSF/Facelets template through its AdminSession class,
 * overriding some methods for that purpose.
 *
 */
@FacesConfig
@Named
@SessionScoped
@Specializes
@PermitAll
public class SessionController extends AdminSession {
    /** Logger for this class. */
    private static final Logger logger = Logger.getLogger(SessionController.class.getCanonicalName());

    /** The login service. */
    @EJB
    private LoginService loginService;

    /** The JSF context. */
    @Inject
    private FacesContext facesContext;

    /** The Jakarta Security context. */
    @SuppressWarnings("CdiInjectionPointsInspection")
    @Inject
    private SecurityContext securityContext;

    /** The authenticated user. */
    private User currentUser;

    /** Input: e-mail for authentication. */
    private String email;

    /** Input: password for authentication. */
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    /**
     * Indicates if the user is an administrator.
     *
     * @return {@code true} if the user has the Admin role, {@code false} otherwise.
     */
    public boolean isAdmin() {
        return Faces.isUserInRole(Role.ADMIN.getValue());
    }

    /**
     * Provides the current authenticated user.
     *
     * @return The User object that represents the user that has been authenticated in this
     * session.
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Controller method that asks the Login service to authenticate the user given her email and
     * password.
     *
     * @return The path to the web page that shows that the operation completed successfully.
     */
    public String login() {
        try {
            // Uses the Session Information bean to authenticate the user.
            logger.log(Level.FINEST, "User attempting login with email \"{0}\"...", email);
            loginService.login(email, password);

            // Also authenticates on Jakarta Security.
            HttpServletRequest request =
                    (HttpServletRequest) facesContext.getExternalContext().getRequest();
            HttpServletResponse response =
                    (HttpServletResponse) facesContext.getExternalContext().getResponse();
            Credential credential = new UsernamePasswordCredential(email, new Password(password));
            AuthenticationStatus status = securityContext.authenticate(request, response,
                    AuthenticationParameters.withParams().credential(credential));

            // Verifies if Jakarta Security also authenticated.
            if (status == null || AuthenticationStatus.SEND_FAILURE.equals(status)) {
                throw new LoginFailedException(LoginFailedException.LoginFailedReason.CONTAINER_REJECTED);
            }
            if (AuthenticationStatus.SEND_CONTINUE.equals(status)) {
                logger.log(Level.FINEST, "Jakarta Security also authenticated user \"{0}\". Back to JSF.",
                        email);
                facesContext.responseComplete();
            }
        } catch (LoginFailedException e) {
            // Checks if it's a normal login exception (wrong username or password) or not.
            switch (e.getReason()) {
                case INCORRECT_PASSWORD, UNKNOWN_USERNAME -> {
                    // Normal login exception (invalid username or password). Report the error to the user.
                    logger.log(Level.INFO, "Login failed for \"{0}\". Reason: \"{1}\"",
                            new Object[] {email, e.getReason()});
                    new MyJSFController().addUnknownCredentialsMessage();
                    return null;
                }
                default -> {
                    // System failure exception. Report a fatal error and ask the user to contact the administrators.
                    logger.log(Level.INFO,
                            "System failure during login. Email: \"" + email + "\"; reason: \"" + e.getReason() + "\"",
                            e);
                    new MyJSFController().addFatalSystemErrorMessage();
                    return null;
                }
            }
        }

        // If everything is OK, stores the current user and redirects back to the home screen.
        currentUser = loginService.getCurrentUser();
        return "/public/index.xhtml?faces-redirect=true";
    }

    /**
     * Circumventing the fact that this controller cannot inherit from JButler's JSFController.
     */
    private static class MyJSFController extends JSFController {
        void addUnknownCredentialsMessage() {
            addGlobalI18nMessage("msgsCore", FacesMessage.SEVERITY_ERROR, "login.error.nomatch.summary",
                    "login.error.nomatch.detail");
        }

        void addFatalSystemErrorMessage() {
            addGlobalI18nMessage("msgsCore", FacesMessage.SEVERITY_FATAL, "login.error.fatal.summary",
                    new Object[0], "login.error.fatal.detail", new Object[] {LocalDateTime.now()});
        }
    }
}

