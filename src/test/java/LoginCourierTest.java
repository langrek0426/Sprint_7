import com.google.gson.Gson;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class LoginCourierTest {

    @Before
    public void setUp () {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    public void checkLoginTest () {
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
        // Убираем значение имени для послед. логина
        courier.setFirstName(null);
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courier)
                        .when()
                        .post("/api/v1/courier/login");
        response.then().assertThat().body("id", notNullValue())
                .and()
                .statusCode(200);
        String id = response.getBody().asString();
        Gson gson = new Gson();
        CourierID courierID = gson.fromJson(id, CourierID.class);
        // Удаляем курьера
        Response response1 = given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courierID)
                        .when()
                        .delete("/api/v1/courier");
    }

    @Test
    public void checkInvalidCredentialsTest() {
        String postfix = Integer.toString(new Random().nextInt(10000));
        String randomPassword = Integer.toString(new Random().nextInt(10000));
        Courier courier = new Courier("login" + postfix, randomPassword, null);
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courier)
                        .when()
                        .post("/api/v1/courier/login");
        response.then().assertThat().body("message", is("Учетная запись не найдена"))
                .and()
                .statusCode(404);
    }

    @Test
    public void checkEmptyPasswordTest() {
        String postfix = Integer.toString(new Random().nextInt(10000));
        Courier courier = new Courier("login" + postfix, null, null);
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courier)
                        .when()
                        .post("/api/v1/courier/login");
        response.then().assertThat().body("message", is("Недостаточно данных для входа"))
                .and()
                .statusCode(400);
    }

    @Test
    public void checkEmptyLoginTest() {
        String randomPassword = Integer.toString(new Random().nextInt(10000));
        Courier courier = new Courier(null, randomPassword, null);
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courier)
                        .when()
                        .post("/api/v1/courier/login");
        response.then().assertThat().body("message", is("Недостаточно данных для входа"))
                .and()
                .statusCode(400);
    }
}
