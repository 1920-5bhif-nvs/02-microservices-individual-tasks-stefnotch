package at.htl;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@QuarkusTest
public class ExampleResourceTest {
    @BeforeEach
    void beforeEach() {
        RestAssured.port = 8080; // Test the other microservice
    }

    @Test
    public void testGetDoctorByNameEndpoint() {
       given()
          .when().get("/api/doctor/name/testDoctor")
          .then()
             .statusCode(200)
             .body("name", equalTo("testDoctor"));
    }

    @Test
    public void testGetPatientEndpoint() {
        given()
                .when().get("/api/patient/")
                .then()
                .statusCode(200)
                .body(containsString("testSubject"));
    }

    @Test
    public void testPostAndDeletePatientEndpoint() {
        int patientCount = given().when().get("/api/patient").then().extract().path("size()");

        given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(Json.createObjectBuilder()
                        .add("name", "Chell")
                        .add("birthdate","2007-10-10")
                        .build()
                        .toString()
                )
                .when().post("/api/patient")
                .then()
                .statusCode(200);

        given()
                .when().get("/api/patient")
                .then()
                .statusCode(200)
                .body("size()", equalTo(patientCount + 1));
    }
}