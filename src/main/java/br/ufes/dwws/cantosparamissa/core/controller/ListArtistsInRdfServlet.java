package br.ufes.dwws.cantosparamissa.core.controller;

import br.ufes.dwws.cantosparamissa.core.domain.Artist;
import br.ufes.dwws.cantosparamissa.core.domain.Music;
import br.ufes.dwws.cantosparamissa.core.persistence.ArtistDAO;
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

@WebServlet(urlPatterns = { "/public/data/artists" })
public class ListArtistsInRdfServlet extends HttpServlet {
    @EJB
    private ArtistDAO artistDAO;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/xml");

        List<Artist> artists = artistDAO.retrieveAll();

        Model model = ModelFactory.createDefaultModel();
        String myNS = "http://localhost:8080/cantosparamissa/public/data/Artist/";
        String moNS = "http://purl.org/ontology/mo/";
        String musicNS = "http://localhost:8080/cantosparamissa/public/data/Music/";

        model.setNsPrefix("mo", moNS);
        model.setNsPrefix("music", musicNS);

        Resource moMusicArtist = model.createResource(moNS + "MusicArtist");
        Resource moMusicalWork = model.createResource(moNS + "MusicalWork");

        Property moTitle = model.createProperty(moNS + "title");
        Property moPerformed = model.createProperty(moNS + "performed");

        for (Artist artist : artists) {
            // Criar recurso do artista
            Resource artistRes = model.createResource(myNS + artist.getId())
                    .addProperty(RDFS.label, artist.getName())
                    .addProperty(RDF.type, moMusicArtist);

            // Adicionar músicas do artista (apenas referências)
            for (Music music : artist.getMusics()) {
                // Criar recurso da música (apenas referência)
                Resource musicRes = model.createResource(musicNS + music.getId())
                        .addProperty(RDFS.label, music.getTitle())
                        .addProperty(RDF.type, moMusicalWork);

                artistRes.addProperty(moPerformed, musicRes);
            }
        }

        try (PrintWriter out = response.getWriter()) {
            model.write(out, "RDF/XML");
        }
    }
}