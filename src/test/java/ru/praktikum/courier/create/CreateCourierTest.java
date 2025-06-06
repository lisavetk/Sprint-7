package ru.praktikum.courier.create;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.courier.CourierSteps;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;
import static ru.praktikum.EnvConf.MESSAGE_COURIER_IS_EXISTS;

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
        CreateCourierRequest request = new CreateCourierRequest(login, password, firstName);
        courierSteps.createCourier(request)
                .then()
                .statusCode(SC_CREATED)
                .body("ok", is(true));
    }

    @Test
    @DisplayName("Create courier POST /api/v1/courier with same data")
    @Description("Return 409 Сonflict and \"message\": \"Этот логин уже используется\"")
    public void createCourierReturn409DataIsExists() {
        CreateCourierRequest request = new CreateCourierRequest(login, password, firstName);
        courierSteps.createCourier(request);
        courierSteps.createCourier(request)
                .then()
                .statusCode(SC_CONFLICT)
                .body("message", containsString(MESSAGE_COURIER_IS_EXISTS));
    }



    @After
    public void tearDown() {
       courierSteps.checkLoginAndDeleteCourier(login, password);
    }
}