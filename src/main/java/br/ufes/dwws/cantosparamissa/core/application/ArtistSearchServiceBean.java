package br.ufes.dwws.cantosparamissa.core.application;

import br.ufes.dwws.cantosparamissa.core.domain.Artist;
import br.ufes.dwws.cantosparamissa.core.persistence.ArtistDAO;
import jakarta.annotation.security.PermitAll;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import java.util.List;

@Stateless
@PermitAll
public class ArtistSearchServiceBean implements ArtistSearchService {
    @EJB
    private ArtistDAO artistDAO;

    @Override
    public List<Artist> searchByName(String artistName) {
        return artistDAO.searchByName(artistName);
    }
}
