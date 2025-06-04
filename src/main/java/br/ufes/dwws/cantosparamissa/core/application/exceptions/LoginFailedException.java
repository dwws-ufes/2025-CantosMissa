package br.ufes.dwws.cantosparamissa.core.application.exceptions;

/**
 * Application exception that represents the fact that the user could not be authenticated.
 *
 */
public class LoginFailedException extends Exception {
    /** Reason for the failed login. */
    private final LoginFailedReason reason;

    public LoginFailedException(LoginFailedReason reason) {
        this.reason = reason;
    }

    public LoginFailedException(Throwable t, LoginFailedReason reason) {
        super(t);
        this.reason = reason;
    }

    public LoginFailedReason getReason() {
        return reason;
    }

    /**
     * Enumeration of the reasons that an authentication might fail.
     *
     * @author <a href="http://labes.inf.ufes.br/equipe/vitor-souza/">Vítor E. Silva Souza</a>
     */
    public enum LoginFailedReason {
        /**
         * The provided username is unknown, i.e., there's no user with the given username in the
         * database.
         */
        UNKNOWN_USERNAME("unknownUsername"),

        /**
         * The provided password is incorrect. The user with the given username has a different
         * password.
         */
        INCORRECT_PASSWORD("incorrectPassword"),

        /**
         * Multiple users with the given username exist in the database, which is an inconsistency!
         */
        MULTIPLE_USERS("multipleUsers"),

        /**
         * Cantos para Missa itself is OK with the authentication, but the Jakarta EE container is not, responding
         * with an exception.
         */
        CONTAINER_REJECTED("containerRejected"),

        /**
         * Cantos para Missa could not check with the container if the authentication is OK because the HTTP
         * request doesn't exist.
         */
        INFRASTRUCTURE_PROBLEMS("infrastructureProblems");

        /** A unique identifier for the reason. */
        private final String id;

        LoginFailedReason(String id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return id;
        }
    }
}
