package br.ufes.dwws.cantosparamissa.core.controller;

import br.ufes.dwws.cantosparamissa.core.application.ManageMusicsService;
import br.ufes.dwws.cantosparamissa.core.domain.Music;
import br.ufes.dwws.cantosparamissa.core.domain.MusicKey;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.PermitAll;
import jakarta.ejb.EJB;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named
@ViewScoped
@PermitAll
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
                            .redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/public/index.xhtml");
                }

            } catch (NumberFormatException e) {
                logger.log(Level.SEVERE, "ID de música inválido: " + idParam, e);
                FacesContext.getCurrentInstance().getExternalContext()
                        .getFlash().put("errorMessage", "ID inválido para a música.");
                try {
                    // Redireciona para a home
                    FacesContext.getCurrentInstance().getExternalContext()
                            .redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/public/index.xhtml");
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
                            .redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/public/index.xhtml");
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
                        .redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/public/index.xhtml");
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Erro ao redirecionar após ausência de ID", e);
            }
        }
    }

    public Music getSelectedEntity() {
        return selectedEntity;
    }

    public List<MusicKey> getAvailableKeys(){
        List<MusicKey> availableKeys = new ArrayList<>();
        String entityKeyValue = selectedEntity.getMusicKey().getValue();
        // Se o tom da música for menor, só pode escolher entre tons menores
        if(entityKeyValue.endsWith("m")){
            availableKeys.add(MusicKey.A_MINOR);
            availableKeys.add(MusicKey.B_FLAT_MINOR);
            availableKeys.add(MusicKey.B_MINOR);
            availableKeys.add(MusicKey.C_MINOR);
            availableKeys.add(MusicKey.C_SHARP_MINOR);
            availableKeys.add(MusicKey.D_MINOR);
            availableKeys.add(MusicKey.D_SHARP_MINOR);
            availableKeys.add(MusicKey.E_MINOR);
            availableKeys.add(MusicKey.F_MINOR);
            availableKeys.add(MusicKey.F_SHARP_MINOR);
            availableKeys.add(MusicKey.G_MINOR);
            availableKeys.add(MusicKey.G_SHARP_MINOR);
        } else { // Mesmo para tom maior
            availableKeys.add(MusicKey.A);
            availableKeys.add(MusicKey.B_FLAT);
            availableKeys.add(MusicKey.B);
            availableKeys.add(MusicKey.C);
            availableKeys.add(MusicKey.D_FLAT);
            availableKeys.add(MusicKey.D);
            availableKeys.add(MusicKey.E_FLAT);
            availableKeys.add(MusicKey.E);
            availableKeys.add(MusicKey.F);
            availableKeys.add(MusicKey.F_SHARP);
            availableKeys.add(MusicKey.G);
            availableKeys.add(MusicKey.A_FLAT);
        }
        return availableKeys;
    }

    public String getYoutubeEmbedUrl() {
        String link = selectedEntity.getYoutubeLink();
        if (link == null || link.isBlank()) return null;

        try {
            if (link.contains("youtube.com/watch?v=")) {
                return "https://www.youtube.com/embed/" + link.split("v=")[1].split("&")[0];
            } else if (link.contains("youtu.be/")) {
                return "https://www.youtube.com/embed/" + link.split("youtu.be/")[1].split("\\?")[0];
            }
        } catch (Exception e) {
            return null; // erro de parsing
        }
        return null;
    }
}
