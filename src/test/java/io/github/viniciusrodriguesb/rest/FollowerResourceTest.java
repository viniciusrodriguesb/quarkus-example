package io.github.viniciusrodriguesb.rest;

import io.github.viniciusrodriguesb.controller.FollowerResource;
import io.github.viniciusrodriguesb.dto.request.follower.FollowerRequest;
import io.github.viniciusrodriguesb.model.Follower;
import io.github.viniciusrodriguesb.model.User;
import io.github.viniciusrodriguesb.repository.FollowerRepository;
import io.github.viniciusrodriguesb.repository.UserRepository;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@TestHTTPEndpoint(FollowerResource.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FollowerResourceTest {

    @Inject
    FollowerRepository followerRepository;

    @Inject
    UserRepository userRepository;
    Long userId;
    Long usuarioParaSeguir;

    @BeforeEach
    @Transactional
    public void setup() {
        var user = new User();
        user.setAge(30);
        user.setName("Fulano");

        userRepository.persist(user);

        userId = user.getId();

        var user2 = new User();
        user.setAge(21);
        user.setName("Joaozinho");

        userRepository.persist(user2);

        usuarioParaSeguir = user2.getId();

        var user3 = new User();
        user.setAge(121);
        user.setName("cicraninho");

        userRepository.persist(user3);

        var follower = new Follower();
        follower.setUserId(user);
        follower.setFollowerId(user3);

        followerRepository.persist(follower);
    }

    @Test
    @Order(1)
    public void seguirUsuarioTest() {
        var follow = new FollowerRequest();
        follow.setFollowerId(usuarioParaSeguir);

        given()
                .contentType(ContentType.JSON)
                .body(follow)
                .pathParam("userId", userId)
                .when()
                .put()
                .then()
                .statusCode(201);
    }

    @Test
    @Order(2)
    public void usuarioNaoExistente() {
        var follow = new FollowerRequest();
        follow.setFollowerId(userId);

        given()
                .contentType(ContentType.JSON)
                .body(follow)
                .pathParam("userId", 0)
                .when()
                .put()
                .then()
                .statusCode(404);
    }

    @Test
    @Order(3)
    public void usuarioSeguirSiMesmoTest() {
        var follow = new FollowerRequest();
        follow.setFollowerId(userId);

        given()
                .contentType(ContentType.JSON)
                .body(follow)
                .pathParam("userId", userId)
                .when()
                .put()
                .then()
                .statusCode(409)
                .body(Matchers.is("Você não pode seguir a si mesmo"));
    }

    @Test
    @Order(4)
    public void deixarDeSeguirTest() {
        given()
                .contentType(ContentType.JSON)
                .queryParam("followerId", usuarioParaSeguir)
                .pathParam("userId", userId)
                .when()
                .delete()
                .then()
                .statusCode(204);
    }

    @Test
    @Order(5)
    public void erroDeixarDeSeguirTest() {
        given()
                .contentType(ContentType.JSON)
                .queryParam("followerId", usuarioParaSeguir)
                .pathParam("userId", 0)
                .when()
                .delete()
                .then()
                .statusCode(404);
    }

    @Test
    @Order(6)
    public void buscarSeguidoresTest() {
        var response = given()
                .contentType(ContentType.JSON)
                .when()
                .pathParam("userId", userId)
                .get()
                .then()
                .extract()
                .response();

        var followersCount = response.jsonPath().get("qntSeguidores");

        assertEquals(200, response.statusCode());
        assertEquals(1, followersCount);
    }
}
