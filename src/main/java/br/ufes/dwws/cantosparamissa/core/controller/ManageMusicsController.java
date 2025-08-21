package br.ufes.dwws.cantosparamissa.core.controller;

import br.ufes.dwws.cantosparamissa.core.application.ManageArtistsService;
import br.ufes.dwws.cantosparamissa.core.application.ManageMusicsService;
import br.ufes.dwws.cantosparamissa.core.domain.*;
import br.ufes.dwws.cantosparamissa.core.persistence.ArtistDAO;
import br.ufes.inf.labes.jbutler.ejb.application.CrudService;
import br.ufes.inf.labes.jbutler.ejb.controller.CrudController;
import br.ufes.inf.labes.jbutler.ejb.controller.PersistentObjectConverterFromId;
import br.ufes.inf.labes.jbutler.ejb.persistence.exceptions.MultiplePersistentObjectsFoundException;
import br.ufes.inf.labes.jbutler.ejb.persistence.exceptions.PersistentObjectNotFoundException;
import jakarta.annotation.security.PermitAll;
import jakarta.ejb.EJB;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;

import java.util.List;

@Named
@ViewScoped
@PermitAll
public class ManageMusicsController extends CrudController<Music> {
    @EJB
    private ManageMusicsService manageMusicsService;

    @EJB
    private ManageArtistsService manageArtistsService;

    private String artistName;

    @Override
    protected CrudService<Music> getCrudService() {
        return manageMusicsService;
    }

    public MusicKey[] getMusicKeyValues() {
        return MusicKey.values();
    }

    public LiturgicalSeason[] getLiturgicalSeasonValues() {
        return LiturgicalSeason.values();
    }

    public SongType[] getSongTypeValues() {
        return SongType.values();
    }

    public List<Artist> completeArtist(String query) {
        return manageArtistsService.findByNameContaining(query);
    }

    public String getArtistName() { return artistName; }

    public void setArtistName(String artistName) { this.artistName = artistName; }

    // Converter used in artist autocomplete field
    private PersistentObjectConverterFromId<Artist> artistConverter;
    @Inject
    void initConverter(ArtistDAO artistDAO) {
        artistConverter = new PersistentObjectConverterFromId<>(artistDAO);
    }
    public PersistentObjectConverterFromId<Artist> getArtistConverter() {
        return artistConverter;
    }

    public void suggestArtist() throws PersistentObjectNotFoundException, MultiplePersistentObjectsFoundException {
        String title = selectedEntity.getTitle();
        if(title != null && title.length() > 2) {
            String query = "PREFIX dbo: <http://dbpedia.org/ontology/>\n" +
                    "PREFIX dbp: <http://dbpedia.org/property/>\n" +
                    "select ?artist_name\n" +
                    "where{\n" +
                    "         ?uri a dbo:Song ;\n" +
                    "                    dbp:name \"" + title + "\"@en ;\n" +
                    "                    dbp:artist ?artist .\n" +
                    "         ?artist dbp:name ?artist_name .\n" +
                    "}\n" +
                    "limit 1";
            QueryExecution queryExecution = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);
            ResultSet results = queryExecution.execSelect();

            if(results.hasNext()) {
                QuerySolution querySolution = results.next();
                Literal artistNameLiteral = querySolution.getLiteral("artist_name");
                artistName = artistNameLiteral.getString();

                try{
                    Artist artist = manageArtistsService.retrieveByName(artistName);
                    selectedEntity.setArtist(artist);
                }
                catch(PersistentObjectNotFoundException | MultiplePersistentObjectsFoundException ignored) {
                }
            }
        }
    }
}
