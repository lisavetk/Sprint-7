package praktikum.courier.create;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.courier.CourierSteps;

import static org.hamcrest.Matchers.*;

@DisplayName("Create courier with valid data")
public class CreateCourierTest {
    CourierSteps courierSteps = new CourierSteps();
    String login;
    String password;
    String firstName;

    @Before
    public void setUp() {
        login = RandomStringUtils.randomAlphabetic(10);
        password = RandomStringUtils.randomAlphabetic(10);
        firstName = RandomStringUtils.randomAlphabetic(10);
        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());
    }

    @Test
    @DisplayName("Create courier POST /api/v1/courier with valid data")
    @Description("Return 201 Created and ok:true")
    public void createCourierReturn201DataIsValid() {
        courierSteps.createCourier(login, password, firstName)
                .then()
                .statusCode(201)
                .body("ok", is(true));
    }

    @Test
    @DisplayName("Create courier POST /api/v1/courier with same data")
    @Description("Return 409 Сonflict and \"message\": \"Этот логин уже используется\"")
    public void createCourierReturn409DataIsExists() {
        courierSteps.createCourier(login, password, firstName);
        courierSteps.createCourier(login, password, firstName)
                .then()
                .statusCode(409)
                .body("message", containsString("Этот логин уже используется"));
    }



    @After
    public void tearDown() {
       courierSteps.checkLoginAndDeleteCourier(login, password);
    }
}