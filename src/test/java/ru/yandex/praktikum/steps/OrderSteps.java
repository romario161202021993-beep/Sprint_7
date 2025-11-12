package ru.yandex.praktikum.steps;

import io.qameta.allure.Step;
import ru.yandex.praktikum.model.*;

import static io.restassured.RestAssured.given;

public class OrderSteps {

    @Step("Создаём заказ через API")
    public static CreateOrderResponse createOrder(Order order) {
        return given()
                .header("Content-type", "application/json")
                .body(order)
                .post("/api/v1/orders")
                .then()
                .extract()
                .body()
                .as(CreateOrderResponse.class);
    }

    @Step("Получаем список заказов через API")
    public static OrdersListResponse getOrdersList() {
        return given()
                .get("/api/v1/orders")
                .then()
                .extract()
                .body()
                .as(OrdersListResponse.class);
    }

    @Step("Принимаем заказ по ID {orderId} с курьером ID {courierId} через API")
    public static AcceptOrderResponse acceptOrder(int orderId, int courierId) {
        return given()
                .put("/api/v1/orders/accept/{id}?courierId={courierId}", orderId, courierId)
                .then()
                .extract()
                .body()
                .as(AcceptOrderResponse.class);
    }

    @Step("Получаем заказ по трек-номеру {trackNumber} через API")
    public static GetOrderByTrackResponse getOrderByTrack(int trackNumber) {
        return given()
                .queryParam("t", trackNumber)
                .get("/api/v1/orders/track")
                .then()
                .extract()
                .body()
                .as(GetOrderByTrackResponse.class);
    }
}