package br.com.ada.conversor.rest.dto;

import br.com.ada.conversor.modelo.Unidade;

public record UnidadeDTO(String id, String nome, String simbolo) {

    public static UnidadeDTO de(Unidade unidade) {
        return new UnidadeDTO(unidade.name(), unidade.getNome(), unidade.getSimbolo());
    }
}
