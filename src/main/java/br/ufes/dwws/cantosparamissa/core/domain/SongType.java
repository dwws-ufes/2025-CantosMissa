package br.ufes.dwws.cantosparamissa.core.domain;

public enum SongType {
    ENTRADA("entrada"),
    ATO_PENITENCIAL("ato_penitencial"),
    HINO_DE_LOUVOR("hino_de_louvor"),
    SALMO_RESPONSORIAL("salmo_responsorial"),
    ACLAMACAO("aclamacao"),
    OFERTORIO("ofertorio"),
    SANTO("santo"),
    CORDEIRO("cordeiro"),
    COMUNHAO("comunhao"),
    ENCERRAMENTO("encerramento"),
    CANTOS_ESPECIAIS("cantos_especiais");

    private final String key;

    SongType(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}