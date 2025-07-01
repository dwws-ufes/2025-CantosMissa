package br.ufes.dwws.cantosparamissa.core.controller;

import br.ufes.dwws.cantosparamissa.core.application.ManageArtistsService;
import br.ufes.dwws.cantosparamissa.core.domain.Artist;
import br.ufes.dwws.cantosparamissa.core.domain.Music;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.PermitAll;
import jakarta.ejb.EJB;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named
@ViewScoped
@PermitAll
public class ViewArtistController implements Serializable {
    private static final Logger logger = Logger.getLogger(ViewArtistController.class.getName());
    
    @EJB
    private ManageArtistsService manageArtistsService;

    private Artist selectedEntity;

    @PostConstruct
    public void init(){
        // Get the artist id from the URL (GET parameter)
        String idParam = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");

        if (idParam != null) {
            try {
                Long id = Long.valueOf(idParam);
                selectedEntity = manageArtistsService.retrieve(id);

                if (selectedEntity == null) {
                    logger.warning("Artista não encontrado para ID: " + id);
                    FacesContext.getCurrentInstance().getExternalContext()
                            .getFlash().put("errorMessage", "Artista não encontrado.");
                    // Redirects to home
                    FacesContext.getCurrentInstance().getExternalContext()
                            .redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/public/index.xhtml");
                }

            } catch (NumberFormatException e) {
                logger.log(Level.SEVERE, "ID de artista inválido: " + idParam, e);
                FacesContext.getCurrentInstance().getExternalContext()
                        .getFlash().put("errorMessage", "ID inválido para a artista.");
                try {
                    // Redirects to home
                    FacesContext.getCurrentInstance().getExternalContext()
                            .redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/public/index.xhtml");
                } catch (Exception ex) {
                    logger.log(Level.SEVERE, "Erro ao redirecionar após falha de ID", ex);
                }
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Erro ao recuperar a artista.", e);
                FacesContext.getCurrentInstance().getExternalContext()
                        .getFlash().put("errorMessage", "Erro ao carregar artista.");
                try {
                    // Redirects to home
                    FacesContext.getCurrentInstance().getExternalContext()
                            .redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/public/index.xhtml");
                } catch (Exception ex) {
                    logger.log(Level.SEVERE, "Erro ao redirecionar após falha geral", ex);
                }
            }
        } else {
            logger.warning("Nenhum ID informado para a artista.");
            FacesContext.getCurrentInstance().getExternalContext()
                    .getFlash().put("errorMessage", "Nenhum ID de artista informado.");
            try {
                // Redirects to home
                FacesContext.getCurrentInstance().getExternalContext()
                        .redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/public/index.xhtml");
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Erro ao redirecionar após ausência de ID", e);
            }
        }
    }

    public Artist getSelectedEntity() {
        return selectedEntity;
    }

    public Set<Music> getMusics() {
        return selectedEntity.getMusics();
    }
}
