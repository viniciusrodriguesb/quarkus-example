package io.github.viniciusrodriguesb.controller;

import io.github.viniciusrodriguesb.dto.request.follower.FollowerRequest;
import io.github.viniciusrodriguesb.dto.response.SeguidoresUsuarioResponse;
import io.github.viniciusrodriguesb.model.Follower;
import io.github.viniciusrodriguesb.services.FollowerService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/users/{userId}/followers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FollowerResource {

    @Inject
    FollowerService followerService;

    @PUT
    public Response seguirUsuario(@PathParam("userId") Long userId, FollowerRequest request) {
        Follower novoFollower = followerService.seguirUsuario(userId, request);
        return Response.status(Response.Status.CREATED).entity(novoFollower).build();
    }

    @GET
    public Response buscarSeguidores(@PathParam("userId") Long userId) {
        SeguidoresUsuarioResponse result = followerService.buscarSeguidores(userId);
        return Response.ok(result).build();
    }

    @DELETE
    public Response deixarDeSeguir(@PathParam("userId") Long userId, @QueryParam("followerId") Long followerId) {
        followerService.deixarDeSeguir(userId, followerId);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

}
