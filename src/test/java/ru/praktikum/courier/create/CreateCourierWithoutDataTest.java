package ru.praktikum.courier.create;

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

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.hamcrest.CoreMatchers.equalTo;

@DisplayName("Create courier without login or password")
@RunWith(Parameterized.class)
public class CreateCourierWithoutDataTest {
    CourierSteps courierSteps = new CourierSteps();
    String login;
    String password;
    String firstName;
    String nullField;

    public CreateCourierWithoutDataTest(String login, String password, String firstName, String nullField) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.nullField = nullField;
    }

    @Parameterized.Parameters(name = "{3}")
    public static Object[][] testData() {
        String randomLogin = RandomStringUtils.randomAlphabetic(10);
        String randomPassword = RandomStringUtils.randomAlphabetic(10);
        String randomFirstName = RandomStringUtils.randomAlphabetic(10);

        return new Object[][] {
                {null, randomPassword, randomFirstName, "without login"},
                {randomLogin, null, randomFirstName, "without password"},
                {"", randomPassword, randomFirstName, "login is empty"},
                {randomLogin, "", randomFirstName, "password is empty"},
        };
    }

    @Before
    public void setUp() {
        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());
    }

    @Test
    @DisplayName("Create courier POST /api/v1/courier without login")
    @Description("Return 400 Bad Request and \"message\": \"Недостаточно данных для создания учетной записи\"")
    public void createCourierReturn400WithoutData() {
        CreateCourierRequest request = new CreateCourierRequest(login, password, firstName);
        courierSteps.createCourier(request)
                .then()
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

   @After
    public void tearDown() {
       if (login != null && password != null) {
           courierSteps.checkLoginAndDeleteCourier(login, password);
       }
    }
}
