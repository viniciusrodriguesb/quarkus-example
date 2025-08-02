package io.github.viniciusrodriguesb.controller;

import io.github.viniciusrodriguesb.dto.ResponseError;
import io.github.viniciusrodriguesb.dto.request.post.CreatePostRequest;
import io.github.viniciusrodriguesb.model.Post;
import io.github.viniciusrodriguesb.services.PostService;
import jakarta.inject.Inject;
import jakarta.validation.Validator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/users/{userId}/posts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PostResource {

    @Inject
    PostService postService;

    @Inject
    Validator validator;

    @POST
    public Response createPost(@PathParam("userId") Long userId, CreatePostRequest request) {
        var violations = validator.validate(request);

        if (!violations.isEmpty()) {
            ResponseError responseError = ResponseError.createFromValidation(violations);
            return Response.status(422).entity(responseError).build();
        }

        Post post = postService.createPost(userId, request);

        if (post == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.CREATED).entity(post).build();
    }

    @GET
    public Response getPosts(@PathParam("userId") Long userId) {
        List<Post> result = postService.listPosts(userId);

        if (result == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(result).build();
    }

}
