package io.github.viniciusrodriguesb.services;

import io.github.viniciusrodriguesb.dto.request.user.CreateUserRequest;
import io.github.viniciusrodriguesb.dto.request.user.GetUserRequest;
import io.github.viniciusrodriguesb.dto.request.user.UpdateUserRequest;
import io.github.viniciusrodriguesb.model.User;
import io.github.viniciusrodriguesb.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class UserService {

    @Inject
    UserRepository userRepository;

    public List<User> obterTodosUsuarios(GetUserRequest request) {

        if (request.getAge() == null && request.getName() == null) {
            return userRepository.listAll();
        }

        StringBuilder queryBuild = new StringBuilder("FROM User u WHERE 1=1");
        Map<String, Object> params = new HashMap<>();

        if (request.getName() != null && !request.getName().isBlank()) {
            String nameLower = request.getName().toLowerCase();
            queryBuild.append(" AND lower(u.name) LIKE :name");
            params.put("name", "%" + nameLower + "%");
        }

        if (request.getAge() != null) {
            queryBuild.append(" AND u.age = :age");
            params.put("age", request.getAge());
        }

        return userRepository.find(queryBuild.toString(), params).list();
    }

    public User obterPorId(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    public User criarUsuario(CreateUserRequest request) {

        User usuario = new User();

        usuario.setAge(request.getAge());
        usuario.setName(request.getName());

        userRepository.persist(usuario);

        return usuario;
    }

    @Transactional
    public void deletarUsuario(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public User alterarUsuario(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id);

        if (user == null) {
            throw new NotFoundException("Usuário não encontrado.");
        }

        if (request.getName() != null && !request.getName().isBlank()) {
            user.setName(request.getName());
        }

        if (request.getAge() != null && request.getAge() > 0) {
            user.setAge(request.getAge());
        }

        return user;
    }

}
