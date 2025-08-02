package io.github.viniciusrodriguesb.services;

import io.github.viniciusrodriguesb.dto.request.follower.FollowerRequest;
import io.github.viniciusrodriguesb.dto.response.SeguidorResponse;
import io.github.viniciusrodriguesb.dto.response.SeguidoresUsuarioResponse;
import io.github.viniciusrodriguesb.exception.ConflictException;
import io.github.viniciusrodriguesb.exception.NotFoundException;
import io.github.viniciusrodriguesb.model.Follower;
import io.github.viniciusrodriguesb.model.User;
import io.github.viniciusrodriguesb.repository.FollowerRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.QueryParam;

import java.util.stream.Collectors;

@ApplicationScoped
public class FollowerService {

    @Inject
    UserService userService;

    @Inject
    FollowerRepository followerRepository;

    @Transactional
    public Follower seguirUsuario(Long userId, FollowerRequest request) {
        User user = userService.obterPorId(userId);
        User userFollow = userService.obterPorId(request.getFollowerId());
        if (user == null || userFollow == null) {
            throw new NotFoundException("Usuário não encontrado.");
        }

        if (user.equals(userFollow)) {
            throw new ConflictException("Você não pode seguir a si mesmo");
        }

        boolean follows = followerRepository.verificarSeguidores(user, userFollow);

        if (follows) {
            throw new IllegalArgumentException("Seguidor já segue este usuário");
        }

        var entity = new Follower();
        entity.setUserId(user);
        entity.setFollowerId(userFollow);

        followerRepository.persist(entity);

        return entity;
    }

    public SeguidoresUsuarioResponse buscarSeguidores(Long userId) {
        var list = followerRepository.buscarSeguidoresPorUsuario(userId);

        if (list == null) {
            throw new NotFoundException("Usuário não encontrado.");
        }

        var responseObj = new SeguidoresUsuarioResponse();
        responseObj.setQntSeguidores(list.size());

        var listaSeguidores = list.stream().map(SeguidorResponse::new).collect(Collectors.toList());

        responseObj.setSeguidores(listaSeguidores);

        return responseObj;
    }

    @Transactional
    public void deixarDeSeguir(Long userId, Long followerId) {
        User user = userService.obterPorId(userId);
        if (user == null) {
            throw new NotFoundException("Usuário não encontrado.");
        }

        followerRepository.deletarPorSeguidorUsuario(userId, followerId);
    }
}
