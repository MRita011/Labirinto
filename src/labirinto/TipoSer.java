package labirinto;

import java.util.Arrays;

public enum TipoSer {
    ANAO('A', "Anão"),
    BRUXA('B', "Bruxa"),
    CAVALEIRO('C', "Cavaleiro"),
    DUENDE('D', "Duende"),
    ELFO('E', "Elfo"),
    FEIJAO('F', "Feijão");

    private final char simbolo;
    private final String nome;

    TipoSer(char simbolo, String nome) {
        this.simbolo = simbolo;
        this.nome = nome;
    }

    public char getSimbolo() {
        return simbolo;
    }

    public String getNome() {
        return nome;
    }

    public static TipoSer fromChar(char c) {
        return Arrays.stream(values())
                .filter(t -> t.simbolo == c)
                .findFirst()
                .orElse(null);
    }
}