package br.com.ada.conversor.rest.dto;

import br.com.ada.conversor.modelo.Categoria;
import br.com.ada.conversor.modelo.Unidade;

import java.util.List;

public record CategoriaDTO(String id, String descricao, List<UnidadeDTO> unidades) {

    public static CategoriaDTO de(Categoria categoria) {
        List<UnidadeDTO> unidades = Unidade.daCategoria(categoria).stream()
                .map(UnidadeDTO::de)
                .toList();
        return new CategoriaDTO(categoria.name(), categoria.getDescricao(), unidades);
    }
}
