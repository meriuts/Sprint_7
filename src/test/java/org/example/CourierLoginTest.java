package org.example;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CourierLoginTest {
    private Courier courier;
    private CourierLogin courierLogin;
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";

        courier = new Courier("meriuts", "1234", "nik");

        given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier");

        courierLogin = new CourierLogin(courier.getLogin(), courier.getPassword());
    }

    @After
    public void deleteCourier () {
        CourierLogin courierLogin = new CourierLogin(courier.getLogin(), courier.getPassword());
        CourierId courierId = given()
                .header("Content-type", "application/json")
                .body(courierLogin)
                .post("/api/v1/courier/login")
                .as(CourierId.class);
        given()
                .delete("/api/v1/courier/" + courierId.getId());
    }

    @Test
    @DisplayName("Аторизация")
    @Description("Проверяет, что курьер может авторизоваться")
    public void CourierCanLogin () {
        given()
                .header("Content-type", "application/json")
                .and()
                .body(courierLogin)
                .when()
                .post("/api/v1/courier/login")
                .then().statusCode(200);
    }

    @Test
    @DisplayName("Авторизация без указания логина")
    @Description("Проверяет, что нельзя авторизоваться без указания логина")
    public void CourierCannotLoginIfLoginEmpty () {
        courierLogin.setLogin("");
        given()
                .header("Content-type", "application/json")
                .and()
                .body(courierLogin)
                .when()
                .post("/api/v1/courier/login")
                .then().statusCode(400)
                .assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация без указания пароля")
    @Description("Проверяет, что нельзя авторизоваться без указания пароля")
    public void CourierCannotLoginIfPasswordEmpty () {
        courierLogin.setPassword("");
        given()
                .header("Content-type", "application/json")
                .and()
                .body(courierLogin)
                .when()
                .post("/api/v1/courier/login")
                .then().statusCode(400)
                .assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация с указанием несуществующего логина")
    @Description("Проверяет, что нельзя авторизоваться, указав логин, которого нет в БД")
    public void CourierCannotLoginIfLoginWrong () {
        courierLogin.setLogin("vsbeth");
        given()
                .header("Content-type", "application/json")
                .and()
                .body(courierLogin)
                .when()
                .post("/api/v1/courier/login")
                .then().statusCode(404)
                .assertThat().body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Авторизация с указанием несоответсвующего логину пароля")
    @Description("Проверяет, что нельзя авторизоваться, указав пароль, которого несоответсвует логину")
    public void CourierCannotLoginIfPasswordWrong () {
        courierLogin.setPassword("7777");
        given()
                .header("Content-type", "application/json")
                .and()
                .body(courierLogin)
                .when()
                .post("/api/v1/courier/login")
                .then().statusCode(404)
                .assertThat().body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Проверка тела ответа при успешной авторизации")
    @Description("Проверяет, что при успешной авторизации возвращается id курьера")
    public void CourierLoginReturnId () {
        given()
                .header("Content-type", "application/json")
                .and()
                .body(courierLogin)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .assertThat().body("id", notNullValue());
    }

}
