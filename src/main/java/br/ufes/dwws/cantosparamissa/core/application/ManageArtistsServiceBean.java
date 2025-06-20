package br.ufes.dwws.cantosparamissa.core.application;

import br.ufes.dwws.cantosparamissa.core.domain.Artist;
import br.ufes.dwws.cantosparamissa.core.persistence.ArtistDAO;
import br.ufes.inf.labes.jbutler.ejb.application.CrudServiceImpl;
import br.ufes.inf.labes.jbutler.ejb.persistence.BaseDAO;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import java.util.List;

@Stateless
@RolesAllowed("ADMIN")
public class ManageArtistsServiceBean extends CrudServiceImpl<Artist> implements ManageArtistsService{
    @EJB
    private ArtistDAO artistDAO;

    @Override
    public BaseDAO<Artist> getDAO() {
        return artistDAO;
    }

    public List<Artist> findByNameContaining(String namePart) {
        return artistDAO.searchByName(namePart);
    }
}
