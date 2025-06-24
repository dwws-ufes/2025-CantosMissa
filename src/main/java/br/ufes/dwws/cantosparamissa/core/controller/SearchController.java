package br.ufes.dwws.cantosparamissa.core.controller;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

@Named
@RequestScoped
public class SearchController {
    private String query;
    private String type = "music"; // ou "artist"

    public String search() {
        if ("artist".equals(type)) {
            return "/core/artists/index.xhtml?q=" + query + "&faces-redirect=true";
        }
        return "/core/musics/index.xhtml?q=" + query + "&faces-redirect=true";
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
