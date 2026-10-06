package br.com.ada.conversor.modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Cada unidade sabe converter seu valor para a unidade base da sua categoria
 * e vice-versa, usando a fórmula linear:
 *
 *     valorNaBase = valor * fator + deslocamento
 *
 * Para quase todas as unidades o deslocamento é zero. Ele só é necessário
 * em temperaturas (ex.: Fahrenheit e Kelvin em relação a Celsius).
 */
public enum Unidade {

    // Comprimento (base: metro)
    MILIMETRO(Categoria.COMPRIMENTO, "mm", "Milímetro", 0.001),
    CENTIMETRO(Categoria.COMPRIMENTO, "cm", "Centímetro", 0.01),
    METRO(Categoria.COMPRIMENTO, "m", "Metro", 1),
    QUILOMETRO(Categoria.COMPRIMENTO, "km", "Quilômetro", 1000),
    POLEGADA(Categoria.COMPRIMENTO, "in", "Polegada", 0.0254),
    PE(Categoria.COMPRIMENTO, "ft", "Pé", 0.3048),
    JARDA(Categoria.COMPRIMENTO, "yd", "Jarda", 0.9144),
    MILHA(Categoria.COMPRIMENTO, "mi", "Milha", 1609.344),

    // Massa (base: quilograma)
    MILIGRAMA(Categoria.MASSA, "mg", "Miligrama", 0.000001),
    GRAMA(Categoria.MASSA, "g", "Grama", 0.001),
    QUILOGRAMA(Categoria.MASSA, "kg", "Quilograma", 1),
    TONELADA(Categoria.MASSA, "t", "Tonelada", 1000),
    ONCA(Categoria.MASSA, "oz", "Onça", 0.028349523125),
    LIBRA(Categoria.MASSA, "lb", "Libra", 0.45359237),

    // Volume (base: litro)
    MILILITRO(Categoria.VOLUME, "ml", "Mililitro", 0.001),
    LITRO(Categoria.VOLUME, "l", "Litro", 1),
    METRO_CUBICO(Categoria.VOLUME, "m3", "Metro cúbico", 1000),
    GALAO_AMERICANO(Categoria.VOLUME, "gal", "Galão (EUA)", 3.785411784),

    // Área (base: metro quadrado)
    CENTIMETRO_QUADRADO(Categoria.AREA, "cm2", "Centímetro quadrado", 0.0001),
    METRO_QUADRADO(Categoria.AREA, "m2", "Metro quadrado", 1),
    HECTARE(Categoria.AREA, "ha", "Hectare", 10_000),
    QUILOMETRO_QUADRADO(Categoria.AREA, "km2", "Quilômetro quadrado", 1_000_000),
    ACRE(Categoria.AREA, "ac", "Acre", 4046.8564224),

    // Temperatura (base: Celsius)
    CELSIUS(Categoria.TEMPERATURA, "C", "Celsius", 1, 0),
    FAHRENHEIT(Categoria.TEMPERATURA, "F", "Fahrenheit", 5.0 / 9.0, -160.0 / 9.0),
    KELVIN(Categoria.TEMPERATURA, "K", "Kelvin", 1, -273.15),

    // Tempo (base: segundo)
    MILISSEGUNDO(Categoria.TEMPO, "ms", "Milissegundo", 0.001),
    SEGUNDO(Categoria.TEMPO, "s", "Segundo", 1),
    MINUTO(Categoria.TEMPO, "min", "Minuto", 60),
    HORA(Categoria.TEMPO, "h", "Hora", 3600),
    DIA(Categoria.TEMPO, "d", "Dia", 86_400),
    SEMANA(Categoria.TEMPO, "sem", "Semana", 604_800),

    // Velocidade (base: metro por segundo)
    METRO_POR_SEGUNDO(Categoria.VELOCIDADE, "m/s", "Metro por segundo", 1),
    QUILOMETRO_POR_HORA(Categoria.VELOCIDADE, "km/h", "Quilômetro por hora", 1000.0 / 3600.0),
    MILHA_POR_HORA(Categoria.VELOCIDADE, "mph", "Milha por hora", 1609.344 / 3600.0),
    NO(Categoria.VELOCIDADE, "kn", "Nó", 1852.0 / 3600.0);

    private final Categoria categoria;
    private final String simbolo;
    private final String nome;
    private final double fator;
    private final double deslocamento;

    Unidade(Categoria categoria, String simbolo, String nome, double fator) {
        this(categoria, simbolo, nome, fator, 0);
    }

    Unidade(Categoria categoria, String simbolo, String nome, double fator, double deslocamento) {
        this.categoria = categoria;
        this.simbolo = simbolo;
        this.nome = nome;
        this.fator = fator;
        this.deslocamento = deslocamento;
    }

    public double paraBase(double valor) {
        return valor * fator + deslocamento;
    }

    public double daBase(double valorNaBase) {
        return (valorNaBase - deslocamento) / fator;
    }

    public static List<Unidade> daCategoria(Categoria categoria) {
        List<Unidade> resultado = new ArrayList<>();
        for (Unidade unidade : values()) {
            if (unidade.categoria == categoria) {
                resultado.add(unidade);
            }
        }
        return resultado;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public String toString() {
        return nome + " (" + simbolo + ")";
    }
}
