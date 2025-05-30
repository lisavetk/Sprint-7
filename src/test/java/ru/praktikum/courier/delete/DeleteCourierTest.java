package ru.praktikum.courier.delete;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.courier.CourierSteps;
import ru.praktikum.courier.create.CreateCourierRequest;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static ru.praktikum.EnvConf.MESSAGE_DELETE_COURIER_WITHOUT_DATA;
import static ru.praktikum.EnvConf.MESSAGE_DELETE_COURIER_WITH_NON_EXISTENT_ID;

@DisplayName("Delete courier with valid data")
public class DeleteCourierTest {

    CourierSteps courierSteps = new CourierSteps();
    String login;
    String password;
    @Before
    public void setUp() {
        login = RandomStringUtils.randomAlphabetic(10);
        password = RandomStringUtils.randomAlphabetic(10);
        String firstName = RandomStringUtils.randomAlphabetic(10);
        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());
        CreateCourierRequest request = new CreateCourierRequest(login, password, firstName);
        courierSteps.createCourier(request);
    }

    @Test
    @DisplayName("Delete courier DELETE /api/v1/courier/{id} with valid data")
    @Description("Return 200 OK and ok:true")
    public void deleteCourierReturn200WithValidData() {
        Response loginResponse = courierSteps.loginCourier(login, password);
        Integer id = loginResponse.path("id");
        courierSteps.deleteCourier(id)
                .statusCode(SC_OK)
                .body("ok", is(true));
    }

    @Test
    @DisplayName("Delete courier DELETE /api/v1/courier/{id} without id")
    @Description("Return 400 Bad Request and \"message\":  \"Недостаточно данных для удаления курьера\"")
    public void deleteCourierReturn400WithoutId() {
        courierSteps.deleteCourierWithoutId()
                .statusCode(SC_BAD_REQUEST)
                .body("message", containsString(MESSAGE_DELETE_COURIER_WITHOUT_DATA));
    }

    @Test
    @DisplayName("Delete courier DELETE /api/v1/courier/{id} with non-existent id")
    @Description("Return 404 Not Found and   \"message\": \"Курьера с таким id нет\"")
    public void deleteCourierReturn404WithNonExistentId() {
        int id = Integer.parseInt(RandomStringUtils.randomNumeric(7));
        courierSteps.deleteCourier(id)
                .statusCode(SC_NOT_FOUND)
                .body("message", containsString(MESSAGE_DELETE_COURIER_WITH_NON_EXISTENT_ID));
    }
}
