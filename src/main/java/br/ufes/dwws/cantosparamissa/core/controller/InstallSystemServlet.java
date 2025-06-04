package br.ufes.dwws.cantosparamissa.core.controller;

import br.ufes.dwws.cantosparamissa.core.application.ConfigureSystemService;
import br.ufes.dwws.cantosparamissa.core.application.exceptions.SystemAlreadyInstalledException;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "InstallSystemServlet", urlPatterns = {"/install"})
public class InstallSystemServlet extends HttpServlet {
    /** Logger for this class. */
    private static final Logger logger =
            Logger.getLogger(InstallSystemServlet.class.getCanonicalName());

    /** Path to the web page to which to redirect after the installation. */
    private static final String LOGIN_VIEW_URL = "/login.xhtml";

    /** The "Configure System" service. */
    @EJB
    private ConfigureSystemService configureSystemService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Requests system installation.
            configureSystemService.installSystem();
        } catch (SystemAlreadyInstalledException e) {
            logger.log(Level.WARNING, "The system has already been installed. New installation aborted.");
        }

        // Redirects the visitor to the login page.
        response.sendRedirect(request.getContextPath() + LOGIN_VIEW_URL);
    }

}
