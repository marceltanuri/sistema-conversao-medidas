package br.com.ada.conversor.web;

import br.com.ada.conversor.modelo.Categoria;
import br.com.ada.conversor.modelo.Unidade;

/**
 * Monta JSON "na mão", só com StringBuilder, para não depender de bibliotecas
 * como Jackson ou Gson.
 */
public final class Json {

    private Json() {
    }

    public static String categoriasComUnidades() {
        StringBuilder json = new StringBuilder("[");
        Categoria[] categorias = Categoria.values();
        for (int i = 0; i < categorias.length; i++) {
            Categoria categoria = categorias[i];
            if (i > 0) {
                json.append(',');
            }
            json.append("{\"id\":").append(texto(categoria.name()))
                    .append(",\"descricao\":").append(texto(categoria.getDescricao()))
                    .append(",\"unidades\":[");

            boolean primeira = true;
            for (Unidade unidade : Unidade.daCategoria(categoria)) {
                if (!primeira) {
                    json.append(',');
                }
                primeira = false;
                json.append("{\"id\":").append(texto(unidade.name()))
                        .append(",\"nome\":").append(texto(unidade.getNome()))
                        .append(",\"simbolo\":").append(texto(unidade.getSimbolo()))
                        .append('}');
            }
            json.append("]}");
        }
        return json.append(']').toString();
    }

    public static String resultado(double valor, Unidade origem, double resultado, Unidade destino) {
        return "{\"valor\":" + valor
                + ",\"origem\":" + texto(origem.getSimbolo())
                + ",\"resultado\":" + resultado
                + ",\"destino\":" + texto(destino.getSimbolo())
                + "}";
    }

    public static String erro(String mensagem) {
        return "{\"erro\":" + texto(mensagem) + "}";
    }

    private static String texto(String valor) {
        String escapado = valor.replace("\\", "\\\\").replace("\"", "\\\"");
        return "\"" + escapado + "\"";
    }
}
