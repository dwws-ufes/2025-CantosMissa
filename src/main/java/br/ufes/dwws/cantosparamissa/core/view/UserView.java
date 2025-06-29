package br.ufes.dwws.cantosparamissa.core.view;

import br.ufes.dwws.cantosparamissa.core.domain.User;
import br.ufes.dwws.cantosparamissa.core.persistence.UserDAO;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Named
@ViewScoped
public class UserView implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<User> users = new ArrayList<>();
    private String query;

    @Inject
    private UserDAO userDAO;

    @PostConstruct
    public void init() {
        Map<String, String> params = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getRequestParameterMap();

        String queryParam = params.get("q");
        if (queryParam != null && !queryParam.isBlank()) {
            query = queryParam.trim();
            users = userDAO.searchByEmail(query);
        }
    }

    public List<User> getUsers() {
        return users;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
