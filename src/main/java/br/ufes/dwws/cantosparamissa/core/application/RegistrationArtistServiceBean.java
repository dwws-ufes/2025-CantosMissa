package br.ufes.dwws.cantosparamissa.core.application;

import br.ufes.dwws.cantosparamissa.core.domain.Artist;
import br.ufes.dwws.cantosparamissa.core.persistence.ArtistDAO;
import br.ufes.inf.labes.jbutler.ejb.application.CrudServiceImpl;
import br.ufes.inf.labes.jbutler.ejb.persistence.BaseDAO;
import jakarta.annotation.security.PermitAll;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

@Stateless
@PermitAll
public class RegistrationArtistServiceBean extends CrudServiceImpl<Artist> implements RegistrationArtistService {
    @EJB
    private ArtistDAO artistDAO;

    @Override
    public BaseDAO<Artist> getDAO() {
        return artistDAO;
    }

    public void registerArtist(Artist artist) {
        artistDAO.save(artist);
    }
}
