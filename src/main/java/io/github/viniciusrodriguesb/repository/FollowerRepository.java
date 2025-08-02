package io.github.viniciusrodriguesb.repository;

import io.github.viniciusrodriguesb.model.Follower;
import io.github.viniciusrodriguesb.model.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class FollowerRepository implements PanacheRepository<Follower> {

    public boolean verificarSeguidores(User user, User userFollow) {
        Map<String, Object> params = new HashMap<>();
        params.put("user", user);
        params.put("userFollow", userFollow);

        StringBuilder queryBuild = new StringBuilder("FROM Follower f WHERE 1=1 " +
                "AND f.userId = :user " +
                "AND f.followerId = :userFollow ");

        var result = find(queryBuild.toString(), params).firstResultOptional();

        return result.isPresent();
    }

    public List<Follower> buscarSeguidoresPorUsuario(Long userId) {
        var query = find("userId.id", userId);
        return query.list();
    }

    public void deletarPorSeguidorUsuario(Long userId, Long followerId) {
        String query = "DELETE FROM Follower f WHERE f.userId.id = :userId AND f.followerId.id = :followerId";
        delete(query, Parameters.with("userId", userId).and("followerId", followerId));
    }

}
