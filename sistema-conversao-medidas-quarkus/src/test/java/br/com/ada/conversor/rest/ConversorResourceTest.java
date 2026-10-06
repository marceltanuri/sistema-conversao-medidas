package br.com.ada.conversor.rest;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;

/**
 * Teste de integração: sobe a aplicação Quarkus e faz requisições HTTP de verdade.
 */
@QuarkusTest
class ConversorResourceTest {

    @Test
    void deveListarCategoriasEUnidades() {
        given()
                .when().get("/api/unidades")
                .then()
                .statusCode(200)
                .body("id", hasItem("TEMPERATURA"))
                .body("find { it.id == 'TEMPERATURA' }.unidades.simbolo", hasItem("F"));
    }

    @Test
    void deveConverterAceitandoVirgula() {
        double resultado = given()
                .queryParam("valor", "98,6")
                .queryParam("origem", "FAHRENHEIT")
                .queryParam("destino", "CELSIUS")
                .when().get("/api/converter")
                .then()
                .statusCode(200)
                .body("origem", equalTo("F"))
                .body("destino", equalTo("C"))
                .extract().jsonPath().getDouble("resultado"); // por padrão o RestAssured leria como float

        assertEquals(37.0, resultado, 0.0001);
    }

    @Test
    void deveRecusarValorInvalido() {
        given()
                .queryParam("valor", "abc")
                .queryParam("origem", "METRO")
                .queryParam("destino", "PE")
                .when().get("/api/converter")
                .then()
                .statusCode(400)
                .body("erro", equalTo("Valor inválido. Exemplo: 12.5"));
    }

    @Test
    void deveRecusarUnidadeDesconhecida() {
        given()
                .queryParam("valor", "1")
                .queryParam("origem", "XYZ")
                .queryParam("destino", "PE")
                .when().get("/api/converter")
                .then()
                .statusCode(400)
                .body("erro", equalTo("Unidade desconhecida."));
    }

    @Test
    void deveRecusarCategoriasDiferentes() {
        given()
                .queryParam("valor", "1")
                .queryParam("origem", "METRO")
                .queryParam("destino", "GRAMA")
                .when().get("/api/converter")
                .then()
                .statusCode(400)
                .body("erro", equalTo("Não é possível converter Comprimento em Massa."));
    }

    @Test
    void deveServirAPaginaInicial() {
        given()
                .when().get("/")
                .then()
                .statusCode(200)
                .contentType("text/html");
    }
}
