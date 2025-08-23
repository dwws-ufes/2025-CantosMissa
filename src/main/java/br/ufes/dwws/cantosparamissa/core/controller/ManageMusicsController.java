package br.ufes.dwws.cantosparamissa.core.controller;

import br.ufes.dwws.cantosparamissa.core.application.LocalTripleStoreService;
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

    @Inject
    private LocalTripleStoreService localTripleStore;

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

    private PersistentObjectConverterFromId<Artist> artistConverter;
    @Inject
    void initConverter(ArtistDAO artistDAO) {
        artistConverter = new PersistentObjectConverterFromId<>(artistDAO);
    }
    public PersistentObjectConverterFromId<Artist> getArtistConverter() {
        return artistConverter;
    }

    /**
     * Sugere artista:
     * 1) tenta no store local (TDB2);
     * 2) se não achar, consulta DBpedia e cacheia localmente.
     */
    public void suggestArtist() throws PersistentObjectNotFoundException, MultiplePersistentObjectsFoundException {
        String title = selectedEntity.getTitle();
        if(title != null && title.length() > 2) {
            // 1) TENTA BUSCAR NO STORE LOCAL (TDB2)
            String localQuery =
                    "PREFIX dbo: <http://dbpedia.org/ontology/>\n" +
                            "PREFIX dbp: <http://dbpedia.org/property/>\n" +
                            "SELECT ?artist_name ?song ?artist\n" +
                            "WHERE {\n" +
                            "  ?song a dbo:Song ;\n" +
                            "        dbp:name \"" + title + "\"@en ;\n" +
                            "        dbp:artist ?artist .\n" +
                            "  ?artist dbp:name ?artist_name .\n" +
                            "}\n" +
                            "LIMIT 1";

            Boolean foundLocally = localTripleStore.readTxn(() -> {
                try (QueryExecution qLocal =
                             QueryExecutionFactory.create(localQuery, localTripleStore.getDataset())) {
                    ResultSet rsLocal = qLocal.execSelect();
                    if (!rsLocal.hasNext()) return false;

                    QuerySolution qs = rsLocal.next();
                    Literal artistNameLiteral = qs.getLiteral("artist_name");
                    artistName = artistNameLiteral.getString();

                    try {
                        Artist artist = manageArtistsService.retrieveByName(artistName);
                        selectedEntity.setArtist(artist);
                    } catch (PersistentObjectNotFoundException | MultiplePersistentObjectsFoundException ignored) { }
                    return true;
                }
            });

            if (foundLocally) {
                return;
            }

            // 2) NÃO ACHOU LOCAL: CONSULTA DBPEDIA E FAZ O CACHE
            String remoteQuery =
                    "PREFIX dbo: <http://dbpedia.org/ontology/>\n" +
                            "PREFIX dbp: <http://dbpedia.org/property/>\n" +
                            // pegamos também ?song e ?artist para poder cachear no TDB2
                            "SELECT ?song ?artist ?artist_name\n" +
                            "WHERE {\n" +
                            "  ?song a dbo:Song ;\n" +
                            "        dbp:name \"" + title + "\"@en ;\n" +
                            "        dbp:artist ?artist .\n" +
                            "  ?artist dbp:name ?artist_name .\n" +
                            "}\n" +
                            "LIMIT 1";

            try (QueryExecution qRemote =
                         QueryExecutionFactory.sparqlService("https://dbpedia.org/sparql", remoteQuery)) {
                ResultSet results = qRemote.execSelect();

                if (results.hasNext()) {
                    QuerySolution qs = results.next();
                    Literal artistNameLiteral = qs.getLiteral("artist_name");
                    artistName = artistNameLiteral.getString();

                    // salva na cache no TDB2
                    if (qs.contains("song") && qs.contains("artist")) {
                        String songUri   = qs.getResource("song").getURI();
                        String artistUri = qs.getResource("artist").getURI();
                        localTripleStore.cacheDbpediaSongArtist(songUri, title, artistUri, artistName);
                    }

                    try {
                        Artist artist = manageArtistsService.retrieveByName(artistName);
                        selectedEntity.setArtist(artist);
                    } catch (PersistentObjectNotFoundException | MultiplePersistentObjectsFoundException ignored) { }
                }
            }
        }
    }
}
