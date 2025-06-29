package br.ufes.dwws.cantosparamissa.core.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A servlet that invalidates the user's session and, therefore, logs her out of the system.
 */
@WebServlet(name = "LogoutServlet", urlPatterns = {"/logout"})
public class LogoutServlet extends HttpServlet {
    /** Logger for this class. */
    private static final Logger logger = Logger.getLogger(LogoutServlet.class.getCanonicalName());

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.log(Level.FINER, "Invalidating a user session...");

        // Destroys the session for this user.
        HttpSession session = request.getSession(false);
        if (session != null)
            session.invalidate();

        // Redirects back to the initial page.
        response.sendRedirect(request.getContextPath() + "/public/index.xhtml");
    }
}
