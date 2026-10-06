package br.com.ada.conversor.rest;

import br.com.ada.conversor.excecao.ConversaoInvalidaException;
import br.com.ada.conversor.rest.dto.ErroDTO;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * Transforma a exceção de negócio em uma resposta HTTP 400 com {"erro": "..."}.
 * Assim o Resource não precisa de try/catch.
 */
@Provider
public class ConversaoInvalidaExceptionMapper implements ExceptionMapper<ConversaoInvalidaException> {

    @Override
    public Response toResponse(ConversaoInvalidaException e) {
        return Response.status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON)
                .entity(new ErroDTO(e.getMessage()))
                .build();
    }
}
