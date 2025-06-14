package br.ufes.dwws.cantosparamissa.core.domain;

public enum Key {
    C("C"),
    C_SHARP("C#"),
    D("D"),
    D_SHARP("D#"),
    E("E"),
    F("F"),
    F_SHARP("F#"),
    G("G"),
    G_SHARP("G#"),
    A("A"),
    A_SHARP("A#"),
    B("B"),
    C_FLAT("Cb"),
    D_FLAT("Db"),
    E_FLAT("Eb"),
    F_FLAT("Fb"),
    G_FLAT("Gb"),
    A_FLAT("Ab"),
    B_FLAT("Bb"),
    C_DOUBLE_SHARP("Cx"),
    D_DOUBLE_SHARP("Dx"),
    E_DOUBLE_SHARP("Ex"),
    F_DOUBLE_SHARP("Fx"),
    G_DOUBLE_SHARP("Gx");

    private final String key;

    Key(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}