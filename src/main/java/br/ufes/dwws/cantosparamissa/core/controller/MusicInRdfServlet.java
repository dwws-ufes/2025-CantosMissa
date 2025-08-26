package br.ufes.dwws.cantosparamissa.core.controller;

import br.ufes.dwws.cantosparamissa.core.domain.Music;
import br.ufes.dwws.cantosparamissa.core.persistence.MusicDAO;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = { "/public/data/Music/*" })
public class MusicInRdfServlet extends HttpServlet {
    @EJB
    private MusicDAO musicDAO;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/xml");

        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "É necessário informar o ID da música.");
            return;
        }

        String idStr = pathInfo.substring(1);
        long id;
        try {
            id = Long.parseLong(idStr);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID inválido.");
            return;
        }

        Music music = musicDAO.retrieveById(id);
        if (music == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Música não encontrada.");
            return;
        }

        Model model = ModelFactory.createDefaultModel();
        String myNS = "http://localhost:8080/cantosparamissa/public/data/Music/";
        String moNS = "http://purl.org/ontology/mo/";
        model.setNsPrefix("mo", moNS);

        Resource moMusicalWork = model.createResource(moNS + "MusicalWork");
        Resource moMusicArtist = model.createResource(moNS + "MusicArtist");
        Resource moLyrics = model.createResource(moNS + "Lyrics");

        Property moKey = model.createProperty(moNS + "key");
        Property moPropLyrics = model.createProperty(moNS + "lyrics");
        Property moText = model.createProperty(moNS + "text");
        Property moPerformer = model.createProperty(moNS + "performer");

        String artistURI = "http://localhost:8080/cantosparamissa/public/data/Artist/" + music.getArtist().getId();
        Resource artistRes = model.createResource(artistURI)
                .addProperty(RDF.type, moMusicArtist)
                .addProperty(RDFS.label, music.getArtist().getName());

        // Music Resource
        model.createResource(myNS + music.getId())
                .addProperty(RDF.type, moMusicalWork)
                .addProperty(RDFS.label, music.getTitle())
                .addProperty(moPerformer, artistRes)
                .addLiteral(moKey, music.getMusicKey().getValue())
                .addProperty(moPropLyrics, model.createResource()
                        .addProperty(RDF.type, moLyrics)
                        .addLiteral(moText, music.getChords()));

        try (PrintWriter out = response.getWriter()) {
            model.write(out, "RDF/XML");
        }
    }
}
