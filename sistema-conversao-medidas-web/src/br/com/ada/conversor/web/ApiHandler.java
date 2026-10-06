package br.com.ada.conversor.web;

import br.com.ada.conversor.excecao.ConversaoInvalidaException;
import br.com.ada.conversor.modelo.Unidade;
import br.com.ada.conversor.servico.ConversorService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Endpoints:
 *   GET /api/unidades                                  -> categorias e suas unidades
 *   GET /api/converter?valor=1&origem=METRO&destino=PE -> resultado da conversão
 */
public class ApiHandler implements HttpHandler {

    private final ConversorService conversor;

    public ApiHandler(ConversorService conversor) {
        this.conversor = conversor;
    }

    @Override
    public void handle(HttpExchange troca) throws IOException {
        if (!"GET".equals(troca.getRequestMethod())) {
            responder(troca, 405, Json.erro("Método não permitido."));
            return;
        }

        String caminho = troca.getRequestURI().getPath();
        switch (caminho) {
            case "/api/unidades" -> responder(troca, 200, Json.categoriasComUnidades());
            case "/api/converter" -> converter(troca);
            default -> responder(troca, 404, Json.erro("Endpoint não encontrado."));
        }
    }

    private void converter(HttpExchange troca) throws IOException {
        Map<String, String> parametros = lerParametros(troca.getRequestURI().getRawQuery());

        try {
            double valor = Double.parseDouble(parametros.getOrDefault("valor", "").replace(',', '.'));
            if (Double.isNaN(valor) || Double.isInfinite(valor)) {
                // parseDouble aceita "NaN" e "Infinity", mas eles não são números válidos em JSON
                throw new NumberFormatException();
            }
            Unidade origem = Unidade.valueOf(parametros.getOrDefault("origem", ""));
            Unidade destino = Unidade.valueOf(parametros.getOrDefault("destino", ""));

            double resultado = conversor.converter(valor, origem, destino);
            responder(troca, 200, Json.resultado(valor, origem, resultado, destino));
        } catch (NumberFormatException e) {
            // Double.parseDouble lança NumberFormatException, que é subclasse de IllegalArgumentException,
            // por isso este catch precisa vir antes do próximo
            responder(troca, 400, Json.erro("Valor inválido. Exemplo: 12.5"));
        } catch (IllegalArgumentException e) {
            responder(troca, 400, Json.erro("Unidade desconhecida."));
        } catch (ConversaoInvalidaException e) {
            responder(troca, 400, Json.erro(e.getMessage()));
        }
    }

    private Map<String, String> lerParametros(String query) {
        Map<String, String> parametros = new HashMap<>();
        if (query == null || query.isEmpty()) {
            return parametros;
        }
        for (String par : query.split("&")) {
            String[] chaveValor = par.split("=", 2);
            String chave = URLDecoder.decode(chaveValor[0], StandardCharsets.UTF_8);
            String valor = chaveValor.length > 1 ? URLDecoder.decode(chaveValor[1], StandardCharsets.UTF_8) : "";
            parametros.put(chave, valor);
        }
        return parametros;
    }

    private void responder(HttpExchange troca, int status, String json) throws IOException {
        byte[] corpo = json.getBytes(StandardCharsets.UTF_8);
        troca.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        troca.sendResponseHeaders(status, corpo.length);
        try (OutputStream saida = troca.getResponseBody()) {
            saida.write(corpo);
        }
    }
}
