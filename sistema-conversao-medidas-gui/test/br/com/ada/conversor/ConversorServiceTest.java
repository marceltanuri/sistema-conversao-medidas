package br.com.ada.conversor;

import br.com.ada.conversor.excecao.ConversaoInvalidaException;
import br.com.ada.conversor.modelo.Unidade;
import br.com.ada.conversor.servico.ConversorService;

/**
 * Testes simples, sem JUnit, para manter o projeto sem dependências externas.
 */
public class ConversorServiceTest {

    private static final double TOLERANCIA = 0.0001;
    private static final ConversorService conversor = new ConversorService();
    private static int falhas = 0;

    public static void main(String[] args) {
        verificar("1 km = 1000 m", conversor.converter(1, Unidade.QUILOMETRO, Unidade.METRO), 1000);
        verificar("12 in = 1 ft", conversor.converter(12, Unidade.POLEGADA, Unidade.PE), 1);
        verificar("1 lb = 0.45359237 kg", conversor.converter(1, Unidade.LIBRA, Unidade.QUILOGRAMA), 0.45359237);
        verificar("1 m3 = 1000 l", conversor.converter(1, Unidade.METRO_CUBICO, Unidade.LITRO), 1000);
        verificar("1 ha = 10000 m2", conversor.converter(1, Unidade.HECTARE, Unidade.METRO_QUADRADO), 10_000);
        verificar("100 C = 212 F", conversor.converter(100, Unidade.CELSIUS, Unidade.FAHRENHEIT), 212);
        verificar("32 F = 0 C", conversor.converter(32, Unidade.FAHRENHEIT, Unidade.CELSIUS), 0);
        verificar("0 C = 273.15 K", conversor.converter(0, Unidade.CELSIUS, Unidade.KELVIN), 273.15);
        verificar("-40 F = -40 C", conversor.converter(-40, Unidade.FAHRENHEIT, Unidade.CELSIUS), -40);
        verificar("2 h = 120 min", conversor.converter(2, Unidade.HORA, Unidade.MINUTO), 120);
        verificar("36 km/h = 10 m/s", conversor.converter(36, Unidade.QUILOMETRO_POR_HORA, Unidade.METRO_POR_SEGUNDO), 10);

        try {
            conversor.converter(1, Unidade.METRO, Unidade.QUILOGRAMA);
            falhar("metro -> quilograma deveria lançar exceção");
        } catch (ConversaoInvalidaException e) {
            System.out.println("[OK]    categorias diferentes lançam exceção");
        }

        System.out.println();
        if (falhas > 0) {
            System.out.println(falhas + " teste(s) falharam.");
            System.exit(1);
        }
        System.out.println("Todos os testes passaram.");
    }

    private static void verificar(String descricao, double obtido, double esperado) {
        if (Math.abs(obtido - esperado) < TOLERANCIA) {
            System.out.println("[OK]    " + descricao);
        } else {
            falhar(descricao + " (obtido: " + obtido + ")");
        }
    }

    private static void falhar(String descricao) {
        falhas++;
        System.out.println("[FALHA] " + descricao);
    }
}
