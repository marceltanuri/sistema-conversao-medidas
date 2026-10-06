package br.com.ada.conversor.modelo;

public enum Categoria {
    COMPRIMENTO("Comprimento"),
    MASSA("Massa"),
    VOLUME("Volume"),
    AREA("Área"),
    TEMPERATURA("Temperatura"),
    TEMPO("Tempo"),
    VELOCIDADE("Velocidade");

    private final String descricao;

    Categoria(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
