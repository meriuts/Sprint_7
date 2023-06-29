package ru.scooter.api;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.scooter.api.client.CourierClient;
import ru.scooter.api.courier.CourierBodyCreateRequest;
import ru.scooter.api.courier.CourierBodyLoginRequest;
import ru.scooter.api.courier.CourierIdResponse;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static ru.scooter.api.BaseUri.BASE_URI;


public class CourierLoginTest {
    private CourierClient courierClient;
    private CourierBodyCreateRequest courierBodyCreateRequest;
    private CourierBodyLoginRequest courierBodyLoginRequest;
    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
        courierClient = new CourierClient();
        courierBodyCreateRequest = new CourierBodyCreateRequest("meriuts", "1234", "nik");
        courierClient.createCourier(courierBodyCreateRequest);
        courierBodyLoginRequest = new CourierBodyLoginRequest(courierBodyCreateRequest.getLogin(), courierBodyCreateRequest.getPassword());
    }

    @After
    public void deleteCourier () {
        CourierIdResponse courierIdResponse = courierClient.getLoginResponse(courierBodyLoginRequest).as(CourierIdResponse.class);
        courierClient.deleteCourier(courierIdResponse.getId());
    }

    @Test
    @DisplayName("Аторизация с валидными данными")
    @Description("Проверяет, что курьер может авторизоваться")
    public void CourierLoginValidData () {
        Response courierLoginValidData = courierClient.getLoginResponse(courierBodyLoginRequest);
        courierLoginValidData.then()
                .statusCode(200)
                .assertThat().body("id", notNullValue());
    }

    @Test
    @DisplayName("Авторизация без указания логина")
    @Description("Проверяет, что нельзя авторизоваться без указания логина")
    public void CourierCannotLoginIfLoginEmpty () {
        courierBodyLoginRequest.setLogin("");
        Response courierLoginValidData = courierClient.getLoginResponse(courierBodyLoginRequest);
        courierLoginValidData.then()
                .statusCode(400)
                .assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация без указания пароля")
    @Description("Проверяет, что нельзя авторизоваться без указания пароля")
    public void CourierCannotLoginIfPasswordEmpty () {
        courierBodyLoginRequest.setPassword("");
        Response courierLoginValidData = courierClient.getLoginResponse(courierBodyLoginRequest);
        courierLoginValidData.then()
                .statusCode(400)
                .assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация с указанием несуществующего в БД логина")
    @Description("Проверяет, что нельзя авторизоваться, указав логин, которого нет в БД")
    public void CourierCannotLoginIfLoginWrong () {
        courierBodyLoginRequest.setLogin("vsbethlkj");
        Response courierLoginValidData = courierClient.getLoginResponse(courierBodyLoginRequest);
        courierLoginValidData.then()
                .statusCode(404)
                .assertThat().body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Авторизация с указанием несоответсвующего логину пароля")
    @Description("Проверяет, что нельзя авторизоваться, указав пароль, который несоответсвует логину")
    public void CourierCannotLoginIfPasswordWrong () {
        courierBodyLoginRequest.setPassword("7777");
        Response courierLoginValidData = courierClient.getLoginResponse(courierBodyLoginRequest);
        courierLoginValidData.then()
                .statusCode(404)
                .assertThat().body("message", equalTo("Учетная запись не найдена"));
    }

}
