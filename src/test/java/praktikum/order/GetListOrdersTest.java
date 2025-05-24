package praktikum.order;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.greaterThan;

@DisplayName("Get list of orders")
public class GetListOrdersTest {

    @Before
    public void setUp() {
        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());
    }

    @Test
    @DisplayName("Get orders GET /api/v1/orders")
    @Description("Return 200 and list of orders exists and isn't null")
    public void getListOfOrders() {
        OrderStep orderStep = new OrderStep();
        orderStep.getOrders()
                .then()
                .statusCode(200)
                .body("orders", notNullValue())
                .body("orders.size()", greaterThan(0));
        }
}
