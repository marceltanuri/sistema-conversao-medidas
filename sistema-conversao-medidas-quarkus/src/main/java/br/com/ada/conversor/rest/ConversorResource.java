package br.com.ada.conversor.rest;

import br.com.ada.conversor.excecao.ConversaoInvalidaException;
import br.com.ada.conversor.modelo.Categoria;
import br.com.ada.conversor.modelo.Unidade;
import br.com.ada.conversor.rest.dto.CategoriaDTO;
import br.com.ada.conversor.rest.dto.ResultadoDTO;
import br.com.ada.conversor.servico.ConversorService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import java.util.Arrays;
import java.util.List;

/**
 * Endpoints:
 *   GET /api/unidades                                  -> categorias e suas unidades
 *   GET /api/converter?valor=1&origem=METRO&destino=PE -> resultado da conversão
 *
 * Compare com ApiHandler e Json da versão web: o Quarkus cuida das rotas,
 * da leitura dos parâmetros e da transformação dos records em JSON.
 */
@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
public class ConversorResource {

    private final ConversorService conversor;

    // injeção de dependência pelo construtor: quem cria o ConversorService é o Quarkus
    public ConversorResource(ConversorService conversor) {
        this.conversor = conversor;
    }

    @GET
    @Path("/unidades")
    public List<CategoriaDTO> listarUnidades() {
        return Arrays.stream(Categoria.values())
                .map(CategoriaDTO::de)
                .toList();
    }

    /**
     * Os parâmetros chegam como String (e não como double/Unidade) para podermos aceitar
     * "1,5" e devolver mensagens de erro claras, em vez do 404 padrão do JAX-RS.
     */
    @GET
    @Path("/converter")
    public ResultadoDTO converter(@QueryParam("valor") String valorTexto,
                                  @QueryParam("origem") String origemTexto,
                                  @QueryParam("destino") String destinoTexto) {
        double valor = lerValor(valorTexto);
        Unidade origem = lerUnidade(origemTexto);
        Unidade destino = lerUnidade(destinoTexto);

        double resultado = conversor.converter(valor, origem, destino);
        return new ResultadoDTO(valor, origem.getSimbolo(), resultado, destino.getSimbolo());
    }

    private double lerValor(String texto) {
        try {
            double valor = Double.parseDouble(texto.trim().replace(',', '.'));
            if (!Double.isNaN(valor) && !Double.isInfinite(valor)) {
                return valor;
            }
        } catch (NullPointerException | NumberFormatException e) {
            // cai na exceção abaixo
        }
        throw new ConversaoInvalidaException("Valor inválido. Exemplo: 12.5");
    }

    private Unidade lerUnidade(String texto) {
        try {
            return Unidade.valueOf(texto);
        } catch (NullPointerException | IllegalArgumentException e) {
            throw new ConversaoInvalidaException("Unidade desconhecida.");
        }
    }
}
