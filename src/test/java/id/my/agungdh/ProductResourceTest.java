package id.my.agungdh;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
class ProductResourceTest {

    private static String createBody(String name, String price, int stock) {
        return """
                {
                  "name": "%s",
                  "price": %s,
                  "stock": %d
                }
                """.formatted(name, price, stock);
    }

    @Test
    void testCreate() {
        given()
                .contentType(ContentType.JSON)
                .body(createBody("Laptop", "1500.00", 10))
                .when().post("/products")
                .then()
                .statusCode(201)
                .header("Location", containsString("/products/"))
                .body("id", notNullValue())
                .body("name", is("Laptop"))
                .body("price", is(1500.00f))
                .body("stock", is(10));
    }

    @Test
    void testList() {
        given()
                .contentType(ContentType.JSON)
                .body(createBody("Mouse", "25.50", 5))
                .when().post("/products")
                .then()
                .statusCode(201);

        given()
                .when().get("/products")
                .then()
                .statusCode(200)
                .body("size()", is(1))
                .body("[0].name", is("Mouse"));
    }

    @Test
    void testGetById() {
        int id = given()
                .contentType(ContentType.JSON)
                .body(createBody("Keyboard", "80.00", 7))
                .when().post("/products")
                .then()
                .statusCode(201)
                .extract().path("id");

        given()
                .when().get("/products/" + id)
                .then()
                .statusCode(200)
                .body("id", is(id))
                .body("name", is("Keyboard"))
                .body("price", is(80.00f))
                .body("stock", is(7));
    }

    @Test
    void testGetByIdNotFound() {
        given()
                .when().get("/products/999999")
                .then()
                .statusCode(404);
    }

    @Test
    void testUpdate() {
        int id = given()
                .contentType(ContentType.JSON)
                .body(createBody("Monitor", "200.00", 3))
                .when().post("/products")
                .then()
                .statusCode(201)
                .extract().path("id");

        given()
                .contentType(ContentType.JSON)
                .body(createBody("Monitor 4K", "350.00", 9))
                .when().put("/products/" + id)
                .then()
                .statusCode(200)
                .body("id", is(id))
                .body("name", is("Monitor 4K"))
                .body("price", is(350.00f))
                .body("stock", is(9));
    }

    @Test
    void testUpdateNotFound() {
        given()
                .contentType(ContentType.JSON)
                .body(createBody("Whatever", "10.00", 1))
                .when().put("/products/999999")
                .then()
                .statusCode(404);
    }

    @Test
    void testDelete() {
        int id = given()
                .contentType(ContentType.JSON)
                .body(createBody("Speaker", "60.00", 2))
                .when().post("/products")
                .then()
                .statusCode(201)
                .extract().path("id");

        given()
                .when().delete("/products/" + id)
                .then()
                .statusCode(204);

        given()
                .when().get("/products/" + id)
                .then()
                .statusCode(404);
    }

    @Test
    void testDeleteNotFound() {
        given()
                .when().delete("/products/999999")
                .then()
                .statusCode(404);
    }
}
