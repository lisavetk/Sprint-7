package praktikum.courier;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import praktikum.courier.createCourier.CreateCourierRequest;
import praktikum.courier.loginCourier.LoginCourierRequest;

import static io.restassured.RestAssured.given;
import static praktikum.EnvConf.BASE_URL;

public class CourierSteps {

    @Step("Send POST request to /api/v1/courier")
    public Response createCourier(String login, String password, String firstName) {
        CreateCourierRequest request = new CreateCourierRequest(login, password, firstName);
        return  given()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URL)
                .body(request)
                .when()
                .post("/api/v1/courier");
    }

    @Step("Send POST request to /api/v1/courier/login")
    public Response loginCourier(String login, String password) {
        LoginCourierRequest request = new LoginCourierRequest(login, password);
        return given()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URL)
                .body(request)
                .when()
                .post("/api/v1/courier/login");
    }

    @Step("Send DELETE request to /api/v1/courier/{id}")
    public ValidatableResponse deleteCourier(int id) {
        return given()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URL)
                .pathParam("id", id)
                .when()
                .delete("/api/v1/courier/{id}")
                .then();

    }

    @Step("Auth with login {login} and delete courier if exists")
    public void checkLoginAndDeleteCourier(String login, String password) {
        Response loginResponse = loginCourier(login, password);
        if (loginResponse.statusCode() == 200) {
            Integer id = loginResponse.path("id");
            if (id != null) {
                deleteCourier(id);
            }
        }
    }

    @Step("Send DELETE request to /api/v1/courier without id")
    public ValidatableResponse deleteCourierWithoutId() {
        return given()
                .baseUri(BASE_URL)
                .when()
                .delete("/api/v1/courier")
                .then();
    }

    @Step("Send POST request to /api/v1/courier")
    public Response createCourier(CreateCourierRequest request) {
        return  given()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URL)
                .body(request)
                .when()
                .post("/api/v1/courier");
    }

}
