package br.ufes.dwws.cantosparamissa.core.domain;

public enum LiturgicalSeason {
    ADVENTO("advento"),
    NATAL("natal"),
    TEMPO_COMUM("tempo_comum"),
    QUARESMA("quaresma"),
    PASCOA("pascoa");

    private final String key;

    LiturgicalSeason(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
