package io.github.viniciusrodriguesb.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ErrorHandler implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable exception) {
        if (exception instanceof NotFoundException) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(exception.getMessage()).build();
        }

        if (exception instanceof ConflictException) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(exception.getMessage()).build();
        }

        if (exception instanceof IllegalArgumentException) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(exception.getMessage()).build();
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Erro interno no servidor").build();
    }
}
