package br.ufes.dwws.cantosparamissa.core.controller;

import br.ufes.dwws.cantosparamissa.core.application.ManageMusicsService;
import br.ufes.dwws.cantosparamissa.core.domain.Music;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named
@ViewScoped
public class ViewMusicController implements Serializable {
    private static final Logger logger = Logger.getLogger(ViewMusicController.class.getName());

    @EJB
    private ManageMusicsService manageMusicsService;

    private Music selectedEntity;

    @PostConstruct
    public void init(){
        // Obtém o id da música da URL (parâmetro GET)
        String idParam = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");

        if (idParam != null) {
            try {
                Long id = Long.valueOf(idParam);
                selectedEntity = manageMusicsService.retrieve(id);

                if (selectedEntity == null) {
                    logger.warning("Música não encontrada para ID: " + id);
                    FacesContext.getCurrentInstance().getExternalContext()
                            .getFlash().put("errorMessage", "Música não encontrada.");
                    // Redireciona para a home
                    FacesContext.getCurrentInstance().getExternalContext()
                            .redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/index.xhtml");
                }

            } catch (NumberFormatException e) {
                logger.log(Level.SEVERE, "ID de música inválido: " + idParam, e);
                FacesContext.getCurrentInstance().getExternalContext()
                        .getFlash().put("errorMessage", "ID inválido para a música.");
                try {
                    // Redireciona para a home
                    FacesContext.getCurrentInstance().getExternalContext()
                            .redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/index.xhtml");
                } catch (Exception ex) {
                    logger.log(Level.SEVERE, "Erro ao redirecionar após falha de ID", ex);
                }
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Erro ao recuperar a música.", e);
                FacesContext.getCurrentInstance().getExternalContext()
                        .getFlash().put("errorMessage", "Erro ao carregar música.");
                try {
                    // Redireciona para a home
                    FacesContext.getCurrentInstance().getExternalContext()
                            .redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/index.xhtml");
                } catch (Exception ex) {
                    logger.log(Level.SEVERE, "Erro ao redirecionar após falha geral", ex);
                }
            }
        } else {
            logger.warning("Nenhum ID informado para a música.");
            FacesContext.getCurrentInstance().getExternalContext()
                    .getFlash().put("errorMessage", "Nenhum ID de música informado.");
            try {
                // Redireciona para a home
                FacesContext.getCurrentInstance().getExternalContext()
                        .redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/index.xhtml");
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Erro ao redirecionar após ausência de ID", e);
            }
        }
    }

    public Music getSelectedEntity() {
        return selectedEntity;
    }
}
