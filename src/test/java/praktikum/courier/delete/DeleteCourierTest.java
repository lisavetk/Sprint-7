package praktikum.courier.delete;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import praktikum.courier.CourierSteps;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

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
        courierSteps.createCourier(login, password, firstName);
    }

    @Test
    @DisplayName("Delete courier DELETE /api/v1/courier/{id} with valid data")
    @Description("Return 200 OK and ok:true")
    public void deleteCourierReturn200WithValidData() {
        Response loginResponse = courierSteps.loginCourier(login, password);
        Integer id = loginResponse.path("id");
        courierSteps.deleteCourier(id)
                .statusCode(200)
                .body("ok", is(true));
    }

    @Test
    @DisplayName("Delete courier DELETE /api/v1/courier/{id} without id")
    @Description("Return 400 Bad Request and \"message\":  \"Недостаточно данных для удаления курьера\"")
    public void deleteCourierReturn400WithoutId() {
        courierSteps.deleteCourierWithoutId()
                .statusCode(400)
                .body("message", containsString("Недостаточно данных для удаления курьера"));
    }

    @Test
    @DisplayName("Delete courier DELETE /api/v1/courier/{id} with non-existent id")
    @Description("Return 404 Not Found and   \"message\": \"Курьера с таким id нет\"")
    public void deleteCourierReturn404WithNonExistentId() {
        int id = Integer.parseInt(RandomStringUtils.randomNumeric(7));
        courierSteps.deleteCourier(id)
                .statusCode(404)
                .body("message", containsString("Курьера с таким id нет"));
    }
}
