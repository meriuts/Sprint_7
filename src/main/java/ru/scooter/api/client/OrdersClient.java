package ru.scooter.api.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrdersClient {
    public static final String CREATE_ORDER = "/api/v1/orders";
    public static final String LIST_ORDER = "/api/v1/orders";

    @Step("Получить тело ответ метода создания заказа")
    public Response getOrderResponse (Object body) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(body)
                .when()
                .post(CREATE_ORDER);
    }

    @Step("Создать заказ")
    public void createOrder (Object body) {
         given()
                .header("Content-type", "application/json")
                .and()
                .body(body)
                .when()
                .post(CREATE_ORDER);
    }

    @Step("Получить ответ со списком заказа")
    public Response getOrderList () {
        return given().get(LIST_ORDER);

    }


}
