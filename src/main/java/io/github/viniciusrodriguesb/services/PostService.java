package io.github.viniciusrodriguesb.services;

import io.github.viniciusrodriguesb.dto.request.post.CreatePostRequest;
import io.github.viniciusrodriguesb.model.Post;
import io.github.viniciusrodriguesb.model.User;
import io.github.viniciusrodriguesb.repository.PostRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class PostService {

    @Inject
    UserService userService;

    @Inject
    PostRepository postRepository;

    @Transactional
    public Post createPost(Long userId, CreatePostRequest request) {

        User user = userService.obterPorId(userId);
        if (user == null) {
            return null;
        }

        Post post = new Post();
        post.setMessage(request.getMessage());
        post.setUser(user);

        postRepository.persist(post);

        return post;
    }

    public List<Post> listPosts(Long userId) {
        Map<String, Object> params = new HashMap<>();

        User user = userService.obterPorId(userId);
        if (user == null) {
            return null;
        }

        params.put("user", user);

        StringBuilder queryBuild = new StringBuilder("FROM Post p WHERE 1=1 " +
                                                     "AND p.user = :user " +
                                                     "ORDER BY p.dateTime DESC");

        return postRepository.find(queryBuild.toString(), params).list();
    }

}
