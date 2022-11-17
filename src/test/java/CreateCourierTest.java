import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;


public class CreateCourierTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    public void checkCreateCourierTest () {
        String postfix = Integer.toString(new Random().nextInt(10000));
        String randomPassword = Integer.toString(new Random().nextInt(10000));
        Courier courier = new Courier("login" + postfix, randomPassword, "name" + postfix);
        Response response =
                given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                        .when()
                        .post("/api/v1/courier");
        response.then().assertThat().body("ok", is(true))
                .and()
                .statusCode(201);
    }

    @Test
    public void checkSameLoginTest () {
        String postfix = Integer.toString(new Random().nextInt(10000));
        String randomPassword = Integer.toString(new Random().nextInt(10000));
        Courier courier = new Courier("login" + postfix, randomPassword, "name" + postfix);
        // Создание курьера
        given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier");
        // Повторное создание курьера с такими же данными
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courier)
                        .when()
                        .post("/api/v1/courier");
        response.then().assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."))
                .and()
                .statusCode(409);
    }

    @Test
    public void checkEmptyPasswordTest() {
        String postfix = Integer.toString(new Random().nextInt(10000));
        Courier courier = new Courier("login" + postfix, null, "name" + postfix);
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courier)
                        .when()
                        .post("/api/v1/courier");
        response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);
    }

    @Test
    public void checkEmptyLoginTest() {
        String postfix = Integer.toString(new Random().nextInt(10000));
        String randomPassword = Integer.toString(new Random().nextInt(10000));
        Courier courier = new Courier(null, randomPassword, "name" + postfix);
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courier)
                        .when()
                        .post("/api/v1/courier");
        response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);
    }

}
