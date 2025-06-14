package br.ufes.dwws.cantosparamissa.core.domain;

import br.ufes.inf.labes.jbutler.ejb.persistence.PersistentObjectSupport;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.awt.*;
import java.util.Set;

@Entity
public class Artist extends PersistentObjectSupport {
    @OneToMany(mappedBy = "artist")
    private Set<Music> musics;

    @NotNull
    @Size(max = 100)
    private String name;

    @Size(max = 200)
    private String image;

    public Artist(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public Artist() {

    }

    @Override
    public String toString() {
        return "Artist{" +
                "name='" + name + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Set<Music> getMusics() {
        return musics;
    }
}
