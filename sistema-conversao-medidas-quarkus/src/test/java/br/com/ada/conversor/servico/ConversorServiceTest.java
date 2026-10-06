package br.com.ada.conversor.servico;

import br.com.ada.conversor.excecao.ConversaoInvalidaException;
import br.com.ada.conversor.modelo.Unidade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Os mesmos casos do teste "manual" das outras versões, agora com JUnit 5.
 * Teste de unidade puro: não sobe o Quarkus, por isso é instantâneo.
 */
class ConversorServiceTest {

    private static final double TOLERANCIA = 0.0001;
    private final ConversorService conversor = new ConversorService();

    @ParameterizedTest(name = "{0} {1} = {3} {2}")
    @CsvSource({
            "1,   QUILOMETRO,          METRO,             1000",
            "12,  POLEGADA,            PE,                1",
            "1,   LIBRA,               QUILOGRAMA,        0.45359237",
            "1,   METRO_CUBICO,        LITRO,             1000",
            "1,   HECTARE,             METRO_QUADRADO,    10000",
            "100, CELSIUS,             FAHRENHEIT,        212",
            "32,  FAHRENHEIT,          CELSIUS,           0",
            "0,   CELSIUS,             KELVIN,            273.15",
            "-40, FAHRENHEIT,          CELSIUS,           -40",
            "2,   HORA,                MINUTO,            120",
            "36,  QUILOMETRO_POR_HORA, METRO_POR_SEGUNDO, 10",
    })
    void deveConverter(double valor, Unidade origem, Unidade destino, double esperado) {
        assertEquals(esperado, conversor.converter(valor, origem, destino), TOLERANCIA);
    }

    @Test
    void naoDeveConverterCategoriasDiferentes() {
        assertThrows(ConversaoInvalidaException.class,
                () -> conversor.converter(1, Unidade.METRO, Unidade.QUILOGRAMA));
    }
}
