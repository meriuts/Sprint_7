package org.example;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateOrderTest {

    private final String firstName;
    private final String lastName;
    private final String address;
    private final int metroStation;
    private final String phone;
    private final int rentTime;
    private final String deliveryDate;
    private final String comment;
    private final String[] color;

    private  CreateOrder createOrder;

    public CreateOrderTest (String firstName, String lastName, String address, int metroStation, String phone, int rentTime, String deliveryDate, String comment, String[] color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] getTestAnswers() {
        return new Object[][]{
                {"Naruto", "Uchiha", "Konoha, 142 apt.", 4, "+7 800 355 35 35", 5, "2023-12-12", "Saske, come back to Konoha", new String[]{"BLACK"}},
                {"Naruto", "Uchiha", "Konoha, 142 apt.", 4, "+7 800 355 35 35", 5, "2023-12-12", "Saske, come back to Konoha", new String[]{"GREY"}},
                {"Naruto", "Uchiha", "Konoha, 142 apt.", 4, "+7 800 355 35 35", 5, "2023-12-12", "Saske, come back to Konoha", new String[]{"BLACK", "GREY"}},
                {"Naruto", "Uchiha", "Konoha, 142 apt.", 4, "+7 800 355 35 35", 5, "2023-12-12", "Saske, come back to Konoha", new String[]{}},

        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
        createOrder = new CreateOrder(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
    }

    @Test
    @DisplayName("Проверка возможности указать цвет самоката")
    @Description("Проверяет, что при заказе самоката можно указать цвет BLACK и/или GREY, не указывать цвет")
    public void createOrderColorBlack() {
        given()
                .header("Content-type", "application/json")
                .and()
                .body(createOrder)
                .when()
                .post("/api/v1/orders")
                .then().statusCode(201)
                .assertThat().body("track", notNullValue());
    }

}

