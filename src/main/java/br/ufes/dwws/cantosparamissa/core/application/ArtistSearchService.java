package br.ufes.dwws.cantosparamissa.core.application;

import br.ufes.dwws.cantosparamissa.core.domain.Artist;
import jakarta.ejb.Local;

import java.util.List;

@Local
public interface ArtistSearchService {
    List<Artist> searchByName(String artistName);
}
