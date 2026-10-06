package br.com.ada.conversor;

import br.com.ada.conversor.cli.MenuCli;
import br.com.ada.conversor.servico.ConversorService;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            MenuCli menu = new MenuCli(scanner, new ConversorService());
            menu.iniciar();
        }
    }
}
