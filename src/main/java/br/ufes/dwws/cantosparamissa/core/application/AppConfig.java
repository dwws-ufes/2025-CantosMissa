package br.ufes.dwws.cantosparamissa.core.application;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.annotation.FacesConfig;
import jakarta.security.enterprise.authentication.mechanism.http.CustomFormAuthenticationMechanismDefinition;
import jakarta.security.enterprise.authentication.mechanism.http.LoginToContinue;
import jakarta.security.enterprise.identitystore.DatabaseIdentityStoreDefinition;

@CustomFormAuthenticationMechanismDefinition(
        loginToContinue = @LoginToContinue(loginPage = "/login.xhtml", useForwardToLogin = false,
                errorPage = ""))
@DatabaseIdentityStoreDefinition(dataSourceLookup = "java:app/datasources/cantosparamissa",
        callerQuery = "select password from User where email = ?",
        groupsQuery = "select role from User where email = ?")
@FacesConfig
@ApplicationScoped
public class AppConfig {
}
