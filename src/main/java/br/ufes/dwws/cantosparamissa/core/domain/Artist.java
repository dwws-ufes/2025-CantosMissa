package br.ufes.dwws.cantosparamissa.core.domain;

import br.ufes.inf.labes.jbutler.ejb.persistence.PersistentObjectSupport;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Set;

@Entity
public class Artist extends PersistentObjectSupport {
    @NotNull
    @Column(unique = true)
    private String name;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Nullable
    private byte[] picture;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "artist", fetch =  FetchType.EAGER)
    private Set<Music> musics;

    public Artist() {
    }

    public Artist(String name, @Nullable byte[] picture) {
        this.name = name;
        this.picture = picture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public Set<Music> getMusics() {
        return musics;
    }

    public void addMusic(Music music) {
        musics.add(music);
    }

    @Override
    public String toString() { return name; }
}
