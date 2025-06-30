package br.ufes.dwws.cantosparamissa.core.application;

import br.ufes.dwws.cantosparamissa.core.domain.Music;
import br.ufes.dwws.cantosparamissa.core.persistence.MusicDAO;
import br.ufes.inf.labes.jbutler.ejb.application.CrudServiceImpl;
import br.ufes.inf.labes.jbutler.ejb.persistence.BaseDAO;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

@Stateless
@PermitAll
public class ManageMusicsServiceBean extends CrudServiceImpl<Music> implements ManageMusicsService{
    @EJB
    private MusicDAO musicDAO;

    @Override
    public BaseDAO<Music> getDAO() {
        return musicDAO;
    }
}
