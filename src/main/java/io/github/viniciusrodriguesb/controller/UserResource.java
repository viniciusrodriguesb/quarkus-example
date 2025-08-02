package io.github.viniciusrodriguesb.controller;

import io.github.viniciusrodriguesb.dto.ResponseError;
import io.github.viniciusrodriguesb.dto.request.user.CreateUserRequest;
import io.github.viniciusrodriguesb.dto.request.user.GetUserRequest;
import io.github.viniciusrodriguesb.dto.request.user.UpdateUserRequest;
import io.github.viniciusrodriguesb.model.User;
import io.github.viniciusrodriguesb.services.UserService;
import jakarta.inject.Inject;
import jakarta.validation.Validator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    UserService userService;

    @Inject
    Validator validator;

    @POST
    public Response createUser(CreateUserRequest request) {
        var violations = validator.validate(request);

        if (!violations.isEmpty()) {
            ResponseError responseError = ResponseError.createFromValidation(violations);
            return Response.status(422).entity(responseError).build();
        }

        User result = userService.criarUsuario(request);
        return Response.status(Response.Status.CREATED).entity(result).build();
    }

    @GET
    public Response getUsers(@BeanParam GetUserRequest request) {
        List<User> result = userService.obterTodosUsuarios(request);

        if (result == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(result).build();
    }

    @GET
    @Path("/{id}")
    public Response getUsers(@PathParam("id") Long id) {
        User result = userService.obterPorId(id);

        if (result == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(result).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateUser(@PathParam("id") Long id, UpdateUserRequest request) {
        if (request == null) {
            return Response.status(400, "Objeto da requisição nulo.").build();
        }

        User result = userService.alterarUsuario(id, request);
        return Response.ok(result).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteUser(@PathParam("id") Long id) {
        userService.deletarUsuario(id);
        return Response.noContent().build();
    }

}
