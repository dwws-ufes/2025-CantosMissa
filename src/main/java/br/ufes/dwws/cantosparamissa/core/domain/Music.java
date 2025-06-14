package br.ufes.dwws.cantosparamissa.core.domain;

import br.ufes.inf.labes.jbutler.ejb.persistence.PersistentObjectSupport;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Music extends PersistentObjectSupport {
    @ManyToOne
    private Artist artist;

    @NotNull
    @Size(max = 100)
    private String title;

    @NotNull
    private String chords;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Key key;

    @Enumerated(EnumType.STRING)
    private LiturgicalSeason season;

    @Enumerated(EnumType.STRING)
    private SongType type;

    private String youtubeLink;

    public Music(String title, Artist artist, String chords, Key key, LiturgicalSeason season, SongType type) {
        this.title = title;
        this.artist = artist;
        this.chords = chords;
        this.key = key;
        this.season = season;
        this.type = type;
    }

    public Music() {

    }

    @Override
    public String toString() {
        return "Music{" +
                "title='" + title + '\'' +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public String getChords() {
        return chords;
    }

    public void setChords(String chords) {
        this.chords = chords;
    }

    public LiturgicalSeason getSeason() {
        return season;
    }

    public void setSeason(LiturgicalSeason season) {
        this.season = season;
    }

    public SongType getType() {
        return type;
    }

    public void setType(SongType type) {
        this.type = type;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public String getYoutubeLink() {
        return youtubeLink;
    }

    public void setYoutubeLink(String youtubeLink) {
        this.youtubeLink = youtubeLink;
    }
}
