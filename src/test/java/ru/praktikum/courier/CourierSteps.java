package ru.praktikum.courier;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import ru.praktikum.courier.create.CreateCourierRequest;
import ru.praktikum.courier.login.LoginCourierRequest;

import static io.restassured.RestAssured.given;
import static ru.praktikum.Endpoints.*;
import static ru.praktikum.EnvConf.BASE_URL;

public class CourierSteps {

    // @Step("Send POST request to /api/v1/courier")
   /* public Response createCourier(String login, String password, String firstName) {
        CreateCourierRequest request = new CreateCourierRequest(login, password, firstName);
        return  given()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URL)
                .body(request)
                .when()
                .post(API_CREATE_COURIER);
    }*/

    @Step("Send POST request to /api/v1/courier/login")
    public Response loginCourier(String login, String password) {
        LoginCourierRequest request = new LoginCourierRequest(login, password);
        return given()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URL)
                .body(request)
                .when()
                .post(API_LOGIN_COURIER);
    }

    @Step("Send DELETE request to /api/v1/courier/{id}")
    public ValidatableResponse deleteCourier(int id) {
        return given()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URL)
                .pathParam("id", id)
                .when()
                .delete(API_DELETE_COURIER)
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
                .delete(API_DELETE_COURIER_WITHOUT_LOGIN)
                .then();
    }

    @Step("Send POST request to /api/v1/courier")
    public Response createCourier(CreateCourierRequest request) {
        return  given()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URL)
                .body(request)
                .when()
                .post(API_CREATE_COURIER);
    }

}
