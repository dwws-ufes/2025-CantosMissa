package br.ufes.dwws.cantosparamissa.core.domain;

public enum LiturgicalSeason {
    ADVENTO("Advento"), NATAL("Natal"), TEMPO_COMUM("Tempo Comum"),
    QUARESMA("Quaresma"), PASCOA("Páscoa");

    private final String value;

    LiturgicalSeason(String value) { this.value = value; }

    public String getValue() { return value; }

}
