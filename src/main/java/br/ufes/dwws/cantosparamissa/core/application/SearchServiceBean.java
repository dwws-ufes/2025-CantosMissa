package br.ufes.dwws.cantosparamissa.core.application;

import br.ufes.dwws.cantosparamissa.core.domain.Artist;
import br.ufes.dwws.cantosparamissa.core.domain.Music;
import br.ufes.dwws.cantosparamissa.core.persistence.ArtistDAO;
import br.ufes.dwws.cantosparamissa.core.persistence.MusicDAO;
import jakarta.annotation.security.PermitAll;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import java.util.List;

@Stateless
@PermitAll
public class SearchServiceBean implements SearchService {
    @EJB
    private MusicDAO musicDAO;

    @EJB
    private ArtistDAO artistDAO;

    @Override
    public List<Music> searchByTitle(String title) {
        return musicDAO.searchByTitle(title);
    }

    @Override
    public List<Artist> searchByName(String name) {
        return artistDAO.searchByName(name);
    }
}
