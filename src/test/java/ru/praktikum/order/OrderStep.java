package ru.praktikum.order;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

import java.util.List;

import static io.restassured.RestAssured.given;
import static ru.praktikum.Endpoints.*;
import static ru.praktikum.EnvConf.BASE_URL;

public class OrderStep {


    @Step("Send POST request to /api/v1/orders")
    public Response createOrder(String firstName, String lastName, String address, String metroStation, String phone,
                                int rentTime, String deliveryDate, String comment, List<String> color) {
        CreateOrderRequest request = new CreateOrderRequest(firstName, lastName, address, metroStation, phone,
                rentTime, deliveryDate, comment, color);
        return given()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URL)
                .body(request)
                .when()
                .post(API_CREATE_ORDER);
    }

    @Step("Send PUT request to /api/v1/orders/cancel")
    public ValidatableResponse cancelOrder(cancelOrderRequest request) {
        return given()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URL)
                .body(request)
                .when()
                .put(API_ORDER_CANCEL)
                .then();
    }

    @Step("Send GET request to /api/v1/orders")
    public Response getOrders() {
        return given()
                .baseUri(BASE_URL)
                .when()
                .get(API_GET_ORDER);
    }
}
