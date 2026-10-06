package br.com.ada.conversor.excecao;

public class ConversaoInvalidaException extends RuntimeException {

    public ConversaoInvalidaException(String mensagem) {
        super(mensagem);
    }
}
