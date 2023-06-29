package ru.scooter.api.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CourierClient {
    public static final String CREATE_COURIER = "/api/v1/courier";
    public static final String LOGIN = "/api/v1/courier/login";

    public static final String DELETE_COURIER = "/api/v1/courier/";


    @Step("Создать курьера")
    public void createCourier (Object body) {
         given()
                .header("Content-type", "application/json")
                .and()
                .body(body)
                .when()
                .post(CREATE_COURIER);
    }

    @Step("Авторизовать курьера")
    public void loginCourier (Object body) {
        given()
                .header("Content-type", "application/json")
                .and()
                .body(body)
                .when()
                .post(LOGIN);
    }

    @Step("Получить тело ответ при создании курьера")
    public Response getCreateCourierResponse (Object body) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(body)
                .when()
                .post(CREATE_COURIER);
    }

    @Step("Получить тело ответ при авторизации")
    public Response getLoginResponse (Object body) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(body)
                .when()
                .post(LOGIN);
    }

    @Step("Удалить куьера")
    public void deleteCourier (int id) {
        given().delete(DELETE_COURIER + id);
    }


}
