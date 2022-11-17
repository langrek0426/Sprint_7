import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
public class GetOrdersTest {
    @Before
    public void setUp () {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    public void checkGetOrdersTest() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .when()
                        .get("/api/v1/orders");
        response.then().assertThat().body("orders", notNullValue())
                .and()
                .statusCode(200);
    }
}
