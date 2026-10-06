package br.com.ada.conversor.web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

/**
 * Entrega os arquivos da pasta "public" (HTML, CSS e JavaScript) para o navegador.
 */
public class ArquivoEstaticoHandler implements HttpHandler {

    private static final Map<String, String> TIPOS = Map.of(
            "html", "text/html; charset=utf-8",
            "css", "text/css; charset=utf-8",
            "js", "text/javascript; charset=utf-8");

    private final Path pastaPublica;

    public ArquivoEstaticoHandler(Path pastaPublica) {
        this.pastaPublica = pastaPublica.toAbsolutePath().normalize();
    }

    @Override
    public void handle(HttpExchange troca) throws IOException {
        String caminho = troca.getRequestURI().getPath();
        if (caminho.equals("/")) {
            caminho = "/index.html";
        }

        Path arquivo = pastaPublica.resolve(caminho.substring(1)).normalize();

        // impede pedidos como "/../src/Main.java" de lerem arquivos fora da pasta public
        if (!arquivo.startsWith(pastaPublica) || !Files.isRegularFile(arquivo)) {
            byte[] corpo = "404 - Página não encontrada".getBytes(StandardCharsets.UTF_8);
            troca.getResponseHeaders().set("Content-Type", "text/plain; charset=utf-8");
            troca.sendResponseHeaders(404, corpo.length);
            try (OutputStream saida = troca.getResponseBody()) {
                saida.write(corpo);
            }
            return;
        }

        String nome = arquivo.getFileName().toString();
        String extensao = nome.substring(nome.lastIndexOf('.') + 1);
        byte[] corpo = Files.readAllBytes(arquivo);

        troca.getResponseHeaders().set("Content-Type", TIPOS.getOrDefault(extensao, "application/octet-stream"));
        troca.sendResponseHeaders(200, corpo.length);
        try (OutputStream saida = troca.getResponseBody()) {
            saida.write(corpo);
        }
    }
}
