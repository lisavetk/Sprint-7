package ru.praktikum.courier.login;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.praktikum.courier.CourierSteps;

import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.hamcrest.Matchers.equalTo;

@DisplayName("Login courier with invalid login and password")
@RunWith(Parameterized.class)
public class LoginCourierWithInvalidDataTest {
    CourierSteps courierSteps = new CourierSteps();
    String login;
    String password;
    String description;
    static String validLogin;
    static String validPassword;

    public LoginCourierWithInvalidDataTest(String login, String password, String description) {
        this.login = login;
        this.password = password;
        this.description = description;
    }

    @Parameterized.Parameters(name = "{2}")
    public static Object[][] testData() {
        validLogin = RandomStringUtils.randomAlphabetic(10);
        validPassword = RandomStringUtils.randomAlphabetic(10);
        String firstName = RandomStringUtils.randomAlphabetic(10);
        new CourierSteps().createCourier(validLogin, validPassword, firstName);
        return new Object[][] {
                {RandomStringUtils.randomAlphabetic(10), validPassword, "invalid login"},
                {validLogin, RandomStringUtils.randomAlphabetic(10), "invalid password"},
                {RandomStringUtils.randomAlphabetic(10), RandomStringUtils.randomAlphabetic(10), "invalid login and password"},
        };
    }

    @Before
    public void setUp() {
        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());
    }

    @Test
    @DisplayName("Login courier POST /api/v1/courier/login with invalid data")
    @Description("Return 404 Created and \"message\": \"Учетная запись не найдена\"")
    public void loginCourierReturn404DataIsntExists() {
        courierSteps.loginCourier(login, password)
                .then()
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @AfterClass
    public static void tearDown() {
        new CourierSteps().checkLoginAndDeleteCourier(validLogin, validPassword);
    }
}
