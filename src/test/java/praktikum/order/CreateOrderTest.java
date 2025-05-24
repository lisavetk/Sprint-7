package praktikum.order;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import static org.hamcrest.Matchers.notNullValue;
import static praktikum.EnvConf.COLOR_BLACK;
import static praktikum.EnvConf.COLOR_GREY;

@DisplayName("Create order")
@RunWith(Parameterized.class)
public class CreateOrderTest {
    OrderStep orderStep = new OrderStep();
    String firstName;
    String lastName;
    String address;
    String metroStation;
    String phone;
    int rentTime;
    String deliveryDate;
    String comment;
    List<String> color;
    int track = 0;

    public CreateOrderTest(List<String> color, String chooseColor) {
        this.color = color;
    }

    @Parameterized.Parameters(name = "{1}")
    public static Object[][] testData() {

        return new Object[][] {
                {List.of(COLOR_BLACK), "color is black"},
                {List.of(COLOR_GREY), "color is grey"},
                {List.of(COLOR_BLACK, COLOR_GREY), "color is black and grey"},
                {List.of(), "color is empty"},
        };
    }

    @Before
    public void setUp() {
        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        firstName = RandomStringUtils.randomAlphabetic(10);
        lastName = RandomStringUtils.randomAlphabetic(10);
        address = RandomStringUtils.randomAlphabetic(10);
        metroStation = RandomStringUtils.randomAlphabetic(10);
        phone =  "+7 800 " +
                RandomStringUtils.randomNumeric(3) + " " +
                RandomStringUtils.randomNumeric(2) + " " +
                RandomStringUtils.randomNumeric(2);
        rentTime = Integer.parseInt(RandomStringUtils.randomNumeric(2));
        int randomDays = ThreadLocalRandom.current().nextInt(1, 31);
        deliveryDate =  LocalDate.now().plusDays(randomDays).toString();
        comment = RandomStringUtils.randomAlphabetic(30);


    }

    @Test
    @DisplayName("Create order POST /api/v1/orders with valid data")
    @Description("Return 201 Created and track")
    public void createOrderReturn201ValidData() {
        Response response = orderStep.createOrder(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
        response.then()
                .statusCode(201)
                .body("track", notNullValue());
        track = response.path("track");
    }

    @After
    public void tearDown() {
        if (track != 0) {
            orderStep.cancelOrder(track);
        }
    }
}
