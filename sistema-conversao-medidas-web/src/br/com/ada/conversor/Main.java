package br.com.ada.conversor;

import br.com.ada.conversor.servico.ConversorService;
import br.com.ada.conversor.web.ApiHandler;
import br.com.ada.conversor.web.ArquivoEstaticoHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Path;

public class Main {

    private static final int PORTA = 8881;

    public static void main(String[] args) throws IOException {
        // HttpServer já vem no JDK (módulo jdk.httpserver), sem precisar de Tomcat ou Spring
        HttpServer servidor = HttpServer.create(new InetSocketAddress(PORTA), 0);

        servidor.createContext("/api/", new ApiHandler(new ConversorService()));
        servidor.createContext("/", new ArquivoEstaticoHandler(Path.of("public")));

        servidor.start();
        System.out.println("Servidor rodando em http://localhost:" + PORTA);
        System.out.println("Pressione Ctrl+C para encerrar.");
    }
}
