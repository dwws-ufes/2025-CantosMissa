package br.ufes.dwws.cantosparamissa.core.controller;

import br.ufes.dwws.cantosparamissa.core.application.ManageArtistsService;
import br.ufes.dwws.cantosparamissa.core.domain.Artist;
import br.ufes.inf.labes.jbutler.ejb.application.CrudService;
import br.ufes.inf.labes.jbutler.ejb.controller.CrudController;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;

import java.io.ByteArrayInputStream;

@Named
@ViewScoped
public class ManageArtistsController extends CrudController<Artist> {
    @EJB
    private ManageArtistsService manageArtistsService;

    private UploadedFile uploadedFile;

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    @Override
    protected CrudService<Artist> getCrudService() {
        return manageArtistsService;
    }

    @Override
    protected void prepEntity() {
        if (uploadedFile != null) {
            selectedEntity.setPicture(uploadedFile.getContent());
            System.out.println("Imagem salva: " + uploadedFile.getContent().length + " bytes");
        } else {
            System.out.println("Nenhuma imagem no upload");
        }
    }

    public StreamedContent getArtistImage(Artist artist) {
        if (artist != null && artist.getPicture() != null) {
            System.out.println("Existe imagem");
            return DefaultStreamedContent.builder()
                    .stream(() -> new ByteArrayInputStream(artist.getPicture()))
                    .contentType("image/png")
                    .build();
        } else {
            System.out.println("Nenhuma imagem no upload");
            return null;
        }
    }
}
