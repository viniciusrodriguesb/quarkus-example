package io.github.viniciusrodriguesb.rest;

import io.github.viniciusrodriguesb.controller.PostResource;
import io.github.viniciusrodriguesb.dto.request.post.CreatePostRequest;
import io.github.viniciusrodriguesb.model.Post;
import io.github.viniciusrodriguesb.model.User;
import io.github.viniciusrodriguesb.repository.PostRepository;
import io.github.viniciusrodriguesb.repository.UserRepository;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;

@QuarkusTest
@TestHTTPEndpoint(PostResource.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PostResourceTest {

    @Inject
    PostRepository postRepository;

    @Inject
    UserRepository userRepository;
    Long userId;

    @BeforeEach
    @Transactional
    public void setup() {
        var user = new User();
        user.setAge(30);
        user.setName("Fulano");

        userRepository.persist(user);

        userId = user.getId();

        var post = new Post();
        post.setUser(user);
        post.setMessage("Teste.");

        postRepository.persist(post);
    }

    @Test
    @Order(1)
    public void criarPostTest() {
        var post = new CreatePostRequest();
        post.setMessage("Mensagem.");

        given()
                .contentType(ContentType.JSON)
                .body(post)
                .pathParam("userId", userId)
                .when()
                .post()
                .then()
                .statusCode(201);
    }

    @Test
    @Order(2)
    public void criarPostUsuarioInexistente() {
        var post = new CreatePostRequest();
        post.setMessage("Mensagem.");

        given()
                .contentType(ContentType.JSON)
                .body(post)
                .pathParam("userId", 0)
                .when()
                .post()
                .then()
                .statusCode(404);
    }

    @Test
    @Order(3)
    public void buscarPostsTest() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .pathParam("userId", userId)
                .get()
                .then()
                .statusCode(200)
                .body("size()", Matchers.is(1));
    }

    @Test
    @Order(4)
    public void buscarPostsUsuarioInexistenteTest(){
        given()
                .contentType(ContentType.JSON)
                .when()
                .pathParam("userId", 0)
                .get()
                .then()
                .statusCode(404);
    }

}
