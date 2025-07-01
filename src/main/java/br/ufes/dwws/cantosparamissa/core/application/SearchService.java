package br.ufes.dwws.cantosparamissa.core.application;

import br.ufes.dwws.cantosparamissa.core.domain.Artist;
import br.ufes.dwws.cantosparamissa.core.domain.Music;
import jakarta.ejb.Local;

import java.util.List;

@Local
public interface SearchService {
    List<Music> searchByTitle(String title);
    List<Artist> searchByName(String name);
}
