package io.github.viniciusrodriguesb.rest;

import io.github.viniciusrodriguesb.dto.request.user.CreateUserRequest;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.net.URL;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserResourceTest {

    @TestHTTPResource("/users")
    URL apiUrl;

    @Test
    @Order(1)
    public void criarUsuarioTest() {
        var user = new CreateUserRequest();
        user.setName("Fulano");
        user.setAge(30);

        var response = given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post(apiUrl)
                .then()
                .extract()
                .response();

        assertEquals(201, response.statusCode());
        assertNotNull(response.jsonPath().getString("id"));
    }

    @Test
    @Order(2)
    public void erroCriarUsuarioTest() {
        var user = new CreateUserRequest();
        user.setName(null);
        user.setAge(null);

        var response = given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post(apiUrl)
                .then()
                .extract()
                .response();

        assertEquals(422, response.statusCode());
        assertNotNull("Validation Error", response.jsonPath().getString("message"));
    }

    @Test
    @Order(3)
    public void listarUsuariosTest() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get(apiUrl)
                .then()
                .statusCode(200)
                .body("size()", Matchers.is(1));
    }
}
