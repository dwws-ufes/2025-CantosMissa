package br.ufes.dwws.cantosparamissa.core.domain;

public enum SongType {
    ENTRADA("Entrada"), ATO_PENITENCIAL("Ato Penitencial"),
    HINO_DE_LOUVOR("Hino de Louvor"), SALMO_RESPONSORIAL("Salmo Responsorial"),
    ACLAMACAO("Aclamação"), OFERTORIO("Ofertório"), SANTO("Santo"),
    CORDEIRO("Cordeiro"), COMUNHAO("Comunhão"), ENCERRAMENTO("Encerramento"),
    CANTOS_ESPECIAIS("Cantos Especiais");

    private final String value;

    SongType(String value) { this.value = value; }

    public String getValue() { return value; }
}
