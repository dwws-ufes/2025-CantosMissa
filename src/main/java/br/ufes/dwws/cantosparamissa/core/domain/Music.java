package br.ufes.dwws.cantosparamissa.core.domain;

import br.ufes.inf.labes.jbutler.ejb.persistence.PersistentObjectSupport;
import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Music extends PersistentObjectSupport {
    @NotNull
    @Size(max = 100)
    private String title;

    @NotNull
    private String chords;

    @NotNull
    private Key key;

    @NotNull
    private LiturgicalSeason liturgicalSeason;

    @NotNull
    private SongType songType;

    @Nullable
    private String youtubeLink;

    @ManyToOne
    private Artist artist;

    public Music() {
    }

    public Music(String title, String chords, Key key, LiturgicalSeason liturgicalSeason, SongType songType, @Nullable String youtubeLink) {
        this.title = title;
        this.chords = chords;
        this.key = key;
        this.liturgicalSeason = liturgicalSeason;
        this.songType = songType;
        this.youtubeLink = youtubeLink;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Nullable
    public String getYoutubeLink() {
        return youtubeLink;
    }

    public void setYoutubeLink(@Nullable String youtubeLink) {
        this.youtubeLink = youtubeLink;
    }

    public SongType getSongType() {
        return songType;
    }

    public void setSongType(SongType songType) {
        this.songType = songType;
    }

    public LiturgicalSeason getLiturgicalSeason() {
        return liturgicalSeason;
    }

    public void setLiturgicalSeason(LiturgicalSeason liturgicalSeason) {
        this.liturgicalSeason = liturgicalSeason;
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

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    @Override
    public String toString() {
        return title;
    }
}
