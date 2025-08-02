package io.github.viniciusrodriguesb.repository;

import io.github.viniciusrodriguesb.model.Post;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PostRepository  implements PanacheRepository<Post> {
}
