package org.example;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.example.orderList.OrderList;
import org.junit.Before;
import org.junit.Test;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertTrue;

public class OrderListTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
        CreateOrder createOrder = new CreateOrder("Naruto", "Uchiha", "Konoha, 142 apt.", 4, "+7 800 355 35 35", 5, "2023-12-12", "Saske, come back to Konoha", new String[]{"BLACK"});
        given()
                .header("Content-type", "application/json")
                .and()
                .body(createOrder)
                .when()
                .post("/api/v1/orders");
    }

    @Test
    @DisplayName("Проверка наличия списка заказов")
    @Description("Проверяет, что при в теле ответа возвращается список заказа")
    public void getOrderListTest() {
       OrderList orderList = given()
                .get("/api/v1/orders")
               .body()
               .as(OrderList.class);
       assertTrue(orderList.getOrders().size() > 0);
    }

}
