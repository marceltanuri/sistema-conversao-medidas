package br.com.ada.conversor;

import br.com.ada.conversor.gui.JanelaConversor;
import br.com.ada.conversor.servico.ConversorService;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        // toda manipulação de tela no Swing deve acontecer na thread de eventos (EDT)
        SwingUtilities.invokeLater(() -> {
            JanelaConversor janela = new JanelaConversor(new ConversorService());
            janela.setVisible(true);
        });
    }
}
