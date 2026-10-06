package br.com.ada.conversor.servico;

import br.com.ada.conversor.excecao.ConversaoInvalidaException;
import br.com.ada.conversor.modelo.Unidade;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Mesma regra das versões CLI, GUI e web. A única novidade é o @ApplicationScoped:
 * o Quarkus cria uma única instância e a injeta onde for pedida, em vez de usarmos "new".
 */
@ApplicationScoped
public class ConversorService {

    /**
     * Converte em dois passos: origem -> unidade base -> destino.
     * Assim não é preciso cadastrar uma fórmula para cada par de unidades.
     */
    public double converter(double valor, Unidade origem, Unidade destino) {
        if (origem == null || destino == null) {
            throw new ConversaoInvalidaException("Unidades de origem e destino são obrigatórias.");
        }
        if (origem.getCategoria() != destino.getCategoria()) {
            throw new ConversaoInvalidaException(
                    "Não é possível converter " + origem.getCategoria().getDescricao()
                            + " em " + destino.getCategoria().getDescricao() + ".");
        }

        double valorNaBase = origem.paraBase(valor);
        return destino.daBase(valorNaBase);
    }
}
