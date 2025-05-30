package ru.praktikum.courier.login;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.courier.CourierSteps;
import ru.praktikum.courier.create.CreateCourierRequest;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.apache.http.HttpStatus.*;

@DisplayName("Login courier")
public class LoginCourierTest {
    CourierSteps courierSteps = new CourierSteps();
    String login;
    String password;

    @Before
    public void setUp() {
        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        login = RandomStringUtils.randomAlphabetic(10);
        password = RandomStringUtils.randomAlphabetic(10);
        String firstName = RandomStringUtils.randomAlphabetic(10);
        CreateCourierRequest request = new CreateCourierRequest(login, password, firstName);
        courierSteps.createCourier(request);
    }

    @Test
    @DisplayName("Login courier POST /api/v1/courier/login with valid data")
    @Description("Return 200 Created and id")
    public void loginCourierReturn200AndIdDataIsValid() {
        courierSteps.loginCourier(login, password)
                .then()
                .statusCode(SC_OK)
                .body("id", notNullValue());
    }



    @After
    public void tearDown() {
        courierSteps.checkLoginAndDeleteCourier(login, password);
    }

}
