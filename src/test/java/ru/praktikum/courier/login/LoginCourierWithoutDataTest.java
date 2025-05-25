package ru.praktikum.courier.login;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.praktikum.courier.CourierSteps;
import ru.praktikum.courier.create.CreateCourierRequest;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.hamcrest.Matchers.equalTo;
@DisplayName("Login courier without login or password")
@RunWith(Parameterized.class)
public class LoginCourierWithoutDataTest {
    CourierSteps courierSteps = new CourierSteps();
    String login;
    String password;
    String nullField;
    String randomLogin;
    String randomPassword;
    public LoginCourierWithoutDataTest(String login, String password, String nullField) {
        this.login = login;
        this.password = password;
        this.nullField = nullField;
    }

    @Parameterized.Parameters(name = "{2}")
    public static Object[][] testData() {

        return new Object[][] {
                {null, "valid", "without login"},
                {"", "valid", "login is empty"},
                {"valid", "", "password is empty"},
                {"valid", null, "without password"},
        };
    }

    @Before
    public void setUp() {
        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());
        randomLogin = RandomStringUtils.randomAlphabetic(10);
        randomPassword = RandomStringUtils.randomAlphabetic(10);
        String firstName = RandomStringUtils.randomAlphabetic(10);
        CreateCourierRequest request = new CreateCourierRequest(randomLogin, randomPassword, firstName);
        courierSteps.createCourier(request);
    }

    @Test
    @DisplayName("Login courier POST /api/v1/courier/login without login")
    @Description("Return 400 Bad Request and \"message\": \"Недостаточно данных для входа\"")
    public void loginCourierReturn400WithoutData() {
        String loginTest = "valid".equals(login) ? randomLogin : login;
        String passwordTest = "valid".equals(password) ? randomPassword : password;
        courierSteps.loginCourier(loginTest, passwordTest)
                .then()
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @After
    public void tearDown() {
        courierSteps.checkLoginAndDeleteCourier(randomLogin, randomPassword);
    }
}
