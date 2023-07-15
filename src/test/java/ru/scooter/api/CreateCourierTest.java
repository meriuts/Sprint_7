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

import static org.hamcrest.Matchers.equalTo;
import static ru.scooter.api.BaseUri.BASE_URI;

public class CreateCourierTest {
    private CourierClient courierClient;
    private CourierBodyCreateRequest courierBodyCreateRequest;
    private CourierBodyLoginRequest courierBodyLoginRequest;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
        courierClient = new CourierClient();
        courierBodyCreateRequest = new CourierBodyCreateRequest("meriuts", "1234", "nik");
        courierBodyLoginRequest = new CourierBodyLoginRequest(courierBodyCreateRequest.getLogin(), courierBodyCreateRequest.getPassword());

    }

    @After
    public void deleteCourier () {
        CourierIdResponse courierIdResponse = courierClient.getLoginResponse(courierBodyLoginRequest).as(CourierIdResponse.class);
        courierClient.deleteCourier(courierIdResponse.getId());
    }

    @Test
    @DisplayName("Проверить тело ответа при успешном создании курьера")
    @Description("Запрос возвращает объект ok: true")
    public void createCourierResponseTrue () {
        Response createCourierResponseTrue = courierClient.getCreateCourierResponse(courierBodyCreateRequest);
        createCourierResponseTrue.then()
                .statusCode(201)
                .assertThat().body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Создать двух одинаковых курьера")
    @Description("Проверяет, что нельзя создать двух одинаковым курьера")
    public void createCourierWithSameLogin () {
        courierClient.createCourier(courierBodyCreateRequest);
        Response createCourierWithSameLogin = courierClient.getCreateCourierResponse(courierBodyCreateRequest);
        createCourierWithSameLogin.then()
                .statusCode(409)
                .assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Создать курьера без логина")
    @Description("Проверяет, что нельзя создать курьера без указания логина")
    public void createCourierWithoutLogin () {
        courierBodyCreateRequest.setLogin("");
        Response createCourierWithoutLogin = courierClient.getCreateCourierResponse(courierBodyCreateRequest);
        createCourierWithoutLogin.then()
                .statusCode(400)
                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
    @Test
    @DisplayName("Создать курьера без пароля")
    @Description("Проверяет, что нельзя создать курьера без указания пароля")
    public void createCourierWithoutPassword() {
        courierBodyCreateRequest.setPassword("");
        Response createCourierWithoutPassword = courierClient.getCreateCourierResponse(courierBodyCreateRequest);
        createCourierWithoutPassword.then()
                .statusCode(400)
                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
    @Test
    @DisplayName("Создать курьера без имени")
    @Description("Проверяет, что поле для имени курьера не обязательное")
    public void createCourierWithoutFirstName() {
        courierBodyCreateRequest.setFirstName("");
        Response createCourierWithoutPassword = courierClient.getCreateCourierResponse(courierBodyCreateRequest);
        createCourierWithoutPassword.then()
                .statusCode(201)
                .assertThat().body("ok", equalTo(true));
    }

}
