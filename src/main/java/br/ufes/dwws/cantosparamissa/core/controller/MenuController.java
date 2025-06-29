package br.ufes.dwws.cantosparamissa.core.controller;

import br.ufes.dwws.cantosparamissa.core.domain.LiturgicalSeason;
import br.ufes.dwws.cantosparamissa.core.domain.SongType;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import java.util.Arrays;
import java.util.List;

@Named
@RequestScoped
public class MenuController {

    public List<LiturgicalSeason> getLiturgicalSeasons() {
        return Arrays.asList(LiturgicalSeason.values());
    }

    public List<SongType> getSongTypes() {
        return Arrays.asList(SongType.values());
    }
}
