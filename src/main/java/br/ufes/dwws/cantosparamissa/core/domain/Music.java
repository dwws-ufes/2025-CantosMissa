package br.ufes.dwws.cantosparamissa.core.domain;

import br.ufes.inf.labes.jbutler.ejb.persistence.PersistentObjectSupport;
import jakarta.inject.Named;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Named
public class Music extends PersistentObjectSupport {
    @ManyToOne
    private Artist artist;

    @NotNull
    @Size(max = 100)
    private String title;

    @NotNull
    @Size(max = 100)
    private String chords;

    @Enumerated(EnumType.STRING)
    private MusicKey musicKey;

    @Enumerated(EnumType.STRING)
    private LiturgicalSeason liturgicalSeason;

    @Enumerated(EnumType.STRING)
    private SongType songType;

    @Size(max = 200)
    private String youtubeLink;

    public Music(String title, Artist artist, String chords, MusicKey musicKey, LiturgicalSeason liturgicalSeason, SongType songType) {
        this.title = title;
        this.artist = artist;
        this.chords = chords;
        this.musicKey = musicKey;
        this.liturgicalSeason = liturgicalSeason;
        this.songType = songType;
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

    public MusicKey getKey() {
        return musicKey;
    }

    public void setKey(MusicKey musicKey) {
        this.musicKey = musicKey;
    }

    public String getChords() {
        return chords;
    }

    public void setChords(String chords) {
        this.chords = chords;
    }

    public LiturgicalSeason getSeason() {
        return liturgicalSeason;
    }

    public void setSeason(LiturgicalSeason liturgicalSeason) {
        this.liturgicalSeason = liturgicalSeason;
    }

    public SongType getType() {
        return songType;
    }

    public void setType(SongType songType) {
        this.songType = songType;
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
