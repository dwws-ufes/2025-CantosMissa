package br.ufes.dwws.cantosparamissa.core.application;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.tdb2.TDB2Factory;

@Singleton
@Startup
public class LocalTripleStoreService {

    private Dataset dataset;

    @PostConstruct
    public void init() {
        // triplestore local em target/tdb2
        dataset = TDB2Factory.connectDataset("target/tdb2");
    }

    @PreDestroy
    public void close() {
        if (dataset != null) dataset.close();
    }

    public Dataset getDataset() {
        return dataset;
    }

    public long size() {
        dataset.begin(ReadWrite.READ);
        try {
            return dataset.getDefaultModel().size();
        } finally {
            dataset.end();
        }
    }

    public void clear() {
        dataset.begin(ReadWrite.WRITE);
        try {
            dataset.getDefaultModel().removeAll();
            dataset.commit();
        } finally {
            dataset.end();
        }
    }

    public <T> T readTxn(java.util.function.Supplier<T> body) {
        dataset.begin(org.apache.jena.query.ReadWrite.READ);
        try { return body.get(); }
        finally { dataset.end(); }
    }

    public void writeTxn(Runnable body) {
        dataset.begin(org.apache.jena.query.ReadWrite.WRITE);
        try { body.run(); dataset.commit(); }
        finally { dataset.end(); }
    }

    public void logAllTriples() {
        dataset.begin(ReadWrite.READ);
        try {
            dataset.getDefaultModel().listStatements().forEachRemaining(st -> {
                String s = st.getSubject().toString();
                String p = st.getPredicate().toString();
                String o = st.getObject().toString();
                System.out.println("[RDF] " + s + " " + p + " " + o);
            });
        } finally {
            dataset.end();
        }
    }

    /**
     * Cacheia no triplestore local retornado da DBpedia:
     * - a música (URI), seu título @en e o vínculo dbp:artist
     * - o artista (URI) e seu dbp:name @en
     */
    public void cacheDbpediaSongArtist(String songUri, String songTitleEn,
                                       String artistUri, String artistNameEn) {

        String ttl = """
        @prefix dbo: <http://dbpedia.org/ontology/> .
        @prefix dbp: <http://dbpedia.org/property/> .
        <%s> a dbo:Song ;
             dbp:name "%s"@en ;
             dbp:artist <%s> .
        <%s> dbp:name "%s"@en .
        """.formatted(songUri, escape(songTitleEn), artistUri, artistUri, escape(artistNameEn));

        writeTxn(() -> {
            // lê o TTL em um model temporário
            Model mNew = ModelFactory.createDefaultModel();
            mNew.read(new java.io.StringReader(ttl), null, "TTL");

            // adiciona ao defaultModel sem duplicar
            Model base = dataset.getDefaultModel();
            mNew.listStatements().forEachRemaining(st -> {
                if (!base.contains(st)) base.add(st);
            });
        });
    }

    private static String escape(String s) {
        return s == null ? "" : s.replace("\"", "\\\"");
    }
}