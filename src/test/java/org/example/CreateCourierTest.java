package org.example;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;
public class CreateCourierTest {
    private Courier courier;

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
        courier = new Courier("meriuts", "1234", "nik");
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
    @DisplayName("Проверить код ответа")
    @Description("Запрос возвращает правильный код ответа")
    public void createCourierStatusCode () {
        given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then().statusCode(201);
    }

    @Test
    @DisplayName("Проверить тело ответа при успешном создании курьера")
    @Description("Запрос возвращает объект ok: true")
    public void createCourierResponseTrue () {
        given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then()
                .assertThat().body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Создать двух одинаковых курьера")
    @Description("Проверяет, что нельзя создать двух одинаковым курьера")
    public void createCourierWithSameLogin () {
        given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier");

        given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then().statusCode(409)
                .assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Создать курьера без логина")
    @Description("Проверяет, что нельзя создать курьера без указания логина")
    public void createCourierWithoutLogin () {
        courier.setLogin("");
        given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then().statusCode(400)
                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
    @Test
    @DisplayName("Создать курьера без пароля")
    @Description("Проверяет, что нельзя создать курьера без указания пароля")
    public void createCourierWithoutPassword() {
        courier.setPassword("");
        given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then().statusCode(400)
                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
    @Test
    @DisplayName("Создать курьера без имени")
    @Description("Проверяет, что поле для имени курьера не обязательное")
    public void createCourierWithoutFirstName() {
        courier.setFirstName("");
        given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then().statusCode(201)
                .assertThat().body("ok", equalTo(true));
    }

}
