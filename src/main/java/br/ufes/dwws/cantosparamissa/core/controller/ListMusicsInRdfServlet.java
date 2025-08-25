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
import java.util.List;

@WebServlet(urlPatterns = { "/public/data/musics" })
public class ListMusicsInRdfServlet extends HttpServlet {
    @EJB
    private MusicDAO musicDAO;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/xml");

        List<Music> musics = musicDAO.retrieveAll();

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

        for (Music music : musics) {
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
        }

        try (PrintWriter out = response.getWriter()) {
            model.write(out, "RDF/XML");
        }
    }
}
