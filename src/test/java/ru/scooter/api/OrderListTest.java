package ru.scooter.api;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import ru.scooter.api.client.OrdersClient;
import ru.scooter.api.orders.OrderBodyRequest;
import ru.scooter.api.orders.list.OrderList;
import org.junit.Before;
import org.junit.Test;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertTrue;
import static ru.scooter.api.BaseUri.BASE_URI;

public class OrderListTest {
    private OrdersClient ordersClient;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
        ordersClient = new OrdersClient();
        OrderBodyRequest createOrder = new OrderBodyRequest("Naruto", "Uchiha", "Konoha, 142 apt.", 4, "+7 800 355 35 35", 5, "2023-12-12", "Saske, come back to Konoha", new String[]{"BLACK"});
        ordersClient.createOrder(createOrder);
    }

    @Test
    @DisplayName("Проверка наличия списка заказов")
    @Description("Проверяет, что при в теле ответа возвращается список заказа")
    public void getOrderListTest() {
        OrderList orderList = ordersClient.getOrderList().body().as(OrderList.class);
        assertTrue(orderList.getOrders().size() > 0);

    }

}
