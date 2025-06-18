package br.ufes.dwws.cantosparamissa.core.domain;

public enum MusicKey {
    A("A"), A_MINOR("Am"), B_FLAT("Bb"), B_FLAT_MINOR("Bbm"),
    B("B"), B_MINOR("Bm"), C("C"), C_MINOR("Cm"),
    C_SHARP_MINOR("C#m"), D_FLAT("Db"), D("D"), D_MINOR("Dm"),
    D_SHARP_MINOR("D#m"), E_FLAT("Eb"), E("E"), E_MINOR("Em"),
    F("F"), F_MINOR("Fm"), F_SHARP("F#"), F_SHARP_MINOR("F#m"),
    G("G"), G_MINOR("Gm"), G_SHARP_MINOR("G#m"), A_FLAT("Ab");

    private final String value;

    MusicKey(String value) { this.value = value; }

    public String getValue() { return value; }

}
