import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class MakeOrderTest {
    public String firstName;
    public String lastName;
    public String address;
    public String metroStation;
    public String phone;
    public int rentTime;
    public String deliveryDate;
    public String comment;
    public String[] color;

    public MakeOrderTest(String firstName, String lastName, String address, String metroStation, String phone, int rentTime,
                         String deliveryDate, String comment, String[] color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][]{
                {"John", "Snow", "Black castle", "Wall station", "+79811992323", 4, "2022-12-12", "I dont want it!", new String[]{"BLACK"}},
                {"Daenerys", "Targaryen", "Dragon stone", "Dragon station", "+79811992543", 5, "2022-12-13", "WHERE IS MY DRAGONS?", new String[]{"GREY"}},
                {"Tyrion", "Lannister", "Kings landing", "Casterly rock station", "+79601242299", 6, "2022-12-14", "A Lannister always pays his debts", new String[]{"BLACK", "GREY"}},
                {"Sandor ", "Clegane", "Kings landing", "Hound station", "+79605673473", 6, "2022-12-15", "Fuck the King!", null},
        };
    }

    @Before
    public void setUp () {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    public void makeOrderTest() {
        Order order = new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(order)
                        .when()
                        .post("/api/v1/orders");
        response.then().assertThat().body("track", notNullValue())
                .and()
                .statusCode(201);
    }
}

