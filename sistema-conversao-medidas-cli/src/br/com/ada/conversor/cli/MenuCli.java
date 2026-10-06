package br.com.ada.conversor.cli;

import br.com.ada.conversor.excecao.ConversaoInvalidaException;
import br.com.ada.conversor.modelo.Categoria;
import br.com.ada.conversor.modelo.Unidade;
import br.com.ada.conversor.servico.ConversorService;

import java.util.List;
import java.util.Scanner;

public class MenuCli {

    private final Scanner scanner;
    private final ConversorService conversor;

    public MenuCli(Scanner scanner, ConversorService conversor) {
        this.scanner = scanner;
        this.conversor = conversor;
    }

    public void iniciar() {
        System.out.println("=== Sistema de Conversão de Medidas ===");

        while (true) {
            Categoria categoria = escolherCategoria();
            if (categoria == null) {
                System.out.println("Até logo!");
                return;
            }

            List<Unidade> unidades = Unidade.daCategoria(categoria);
            Unidade origem = escolherUnidade("Unidade de ORIGEM", unidades);
            Unidade destino = escolherUnidade("Unidade de DESTINO", unidades);
            double valor = lerNumero("Valor em " + origem.getSimbolo() + ": ");

            try {
                double resultado = conversor.converter(valor, origem, destino);
                System.out.printf("%n>> %s %s = %s %s%n%n",
                        formatar(valor), origem.getSimbolo(),
                        formatar(resultado), destino.getSimbolo());
            } catch (ConversaoInvalidaException e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }

    private Categoria escolherCategoria() {
        Categoria[] categorias = Categoria.values();

        System.out.println("Escolha uma categoria:");
        for (int i = 0; i < categorias.length; i++) {
            System.out.printf("  %d - %s%n", i + 1, categorias[i].getDescricao());
        }
        System.out.println("  0 - Sair");

        int opcao = lerOpcao(0, categorias.length);
        return opcao == 0 ? null : categorias[opcao - 1];
    }

    private Unidade escolherUnidade(String titulo, List<Unidade> unidades) {
        System.out.println(titulo + ":");
        for (int i = 0; i < unidades.size(); i++) {
            System.out.printf("  %d - %s%n", i + 1, unidades.get(i));
        }

        int opcao = lerOpcao(1, unidades.size());
        return unidades.get(opcao - 1);
    }

    private int lerOpcao(int minimo, int maximo) {
        while (true) {
            System.out.print("Opção: ");
            String entrada = scanner.nextLine().trim();
            try {
                int opcao = Integer.parseInt(entrada);
                if (opcao >= minimo && opcao <= maximo) {
                    return opcao;
                }
            } catch (NumberFormatException e) {
                // cai na mensagem abaixo
            }
            System.out.printf("Opção inválida. Digite um número entre %d e %d.%n", minimo, maximo);
        }
    }

    private double lerNumero(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            // aceita tanto "1,5" quanto "1.5"
            String entrada = scanner.nextLine().trim().replace(',', '.');
            try {
                return Double.parseDouble(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido. Exemplo: 12.5");
            }
        }
    }

    private String formatar(double numero) {
        // remove zeros desnecessários: 2.500000 -> 2.5
        String texto = String.format(java.util.Locale.ROOT, "%.6f", numero);
        texto = texto.replaceAll("0+$", "").replaceAll("\\.$", "");
        return texto.equals("-0") ? "0" : texto;
    }
}
