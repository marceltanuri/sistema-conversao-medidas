package br.com.ada.conversor.gui;

import br.com.ada.conversor.excecao.ConversaoInvalidaException;
import br.com.ada.conversor.modelo.Categoria;
import br.com.ada.conversor.modelo.Unidade;
import br.com.ada.conversor.servico.ConversorService;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Locale;

public class JanelaConversor extends JFrame {

    private final ConversorService conversor;

    private final JComboBox<Categoria> comboCategoria = new JComboBox<>(Categoria.values());
    private final JComboBox<Unidade> comboOrigem = new JComboBox<>();
    private final JComboBox<Unidade> comboDestino = new JComboBox<>();
    private final JTextField campoValor = new JTextField("1", 12);
    private final JLabel labelResultado = new JLabel(" ");

    public JanelaConversor(ConversorService conversor) {
        super("Sistema de Conversão de Medidas");
        this.conversor = conversor;

        montarTela();
        registrarEventos();
        atualizarUnidades();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setResizable(false);
        setLocationRelativeTo(null); // centraliza na tela
    }

    private void montarTela() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.fill = GridBagConstraints.HORIZONTAL;

        adicionarLinha(painel, c, 0, "Categoria:", comboCategoria);
        adicionarLinha(painel, c, 1, "De:", comboOrigem);
        adicionarLinha(painel, c, 2, "Para:", comboDestino);
        adicionarLinha(painel, c, 3, "Valor:", campoValor);

        JButton botaoInverter = new JButton("⇅ Inverter unidades");
        botaoInverter.addActionListener(e -> inverterUnidades());
        c.gridx = 1;
        c.gridy = 4;
        painel.add(botaoInverter, c);

        labelResultado.setFont(labelResultado.getFont().deriveFont(Font.BOLD, 18f));
        c.gridx = 0;
        c.gridy = 5;
        c.gridwidth = 2;
        painel.add(labelResultado, c);

        setContentPane(painel);
    }

    private void adicionarLinha(JPanel painel, GridBagConstraints c, int linha, String texto, java.awt.Component campo) {
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = linha;
        c.weightx = 0;
        painel.add(new JLabel(texto), c);

        c.gridx = 1;
        c.weightx = 1;
        painel.add(campo, c);
    }

    private void registrarEventos() {
        comboCategoria.addActionListener(e -> atualizarUnidades());
        comboOrigem.addActionListener(e -> converter());
        comboDestino.addActionListener(e -> converter());

        // converte a cada tecla digitada, sem precisar de botão "Converter"
        campoValor.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                converter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                converter();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                converter();
            }
        });
    }

    private void atualizarUnidades() {
        Categoria categoria = (Categoria) comboCategoria.getSelectedItem();

        comboOrigem.removeAllItems();
        comboDestino.removeAllItems();
        for (Unidade unidade : Unidade.daCategoria(categoria)) {
            comboOrigem.addItem(unidade);
            comboDestino.addItem(unidade);
        }

        // já sugere um par diferente, para a conversão não começar "1 = 1"
        if (comboDestino.getItemCount() > 1) {
            comboDestino.setSelectedIndex(1);
        }
        converter();
    }

    private void inverterUnidades() {
        Object origem = comboOrigem.getSelectedItem();
        comboOrigem.setSelectedItem(comboDestino.getSelectedItem());
        comboDestino.setSelectedItem(origem);
    }

    private void converter() {
        Unidade origem = (Unidade) comboOrigem.getSelectedItem();
        Unidade destino = (Unidade) comboDestino.getSelectedItem();
        if (origem == null || destino == null) {
            return; // os combos estão sendo recarregados
        }

        String texto = campoValor.getText().trim().replace(',', '.');
        if (texto.isEmpty()) {
            mostrarResultado(" ", Color.BLACK);
            return;
        }

        try {
            double valor = Double.parseDouble(texto);
            double resultado = conversor.converter(valor, origem, destino);
            mostrarResultado(formatar(valor) + " " + origem.getSimbolo()
                    + " = " + formatar(resultado) + " " + destino.getSimbolo(), new Color(0, 110, 0));
        } catch (NumberFormatException e) {
            mostrarResultado("Valor inválido. Exemplo: 12.5", Color.RED);
        } catch (ConversaoInvalidaException e) {
            mostrarResultado(e.getMessage(), Color.RED);
        }
    }

    private void mostrarResultado(String texto, Color cor) {
        labelResultado.setText(texto);
        labelResultado.setForeground(cor);
    }

    private String formatar(double numero) {
        // remove zeros desnecessários: 2.500000 -> 2.5
        String texto = String.format(Locale.ROOT, "%.6f", numero);
        texto = texto.replaceAll("0+$", "").replaceAll("\\.$", "");
        return texto.equals("-0") ? "0" : texto;
    }
}
