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
                .statusCode(201) // Ожидаем 201 для успешного создания
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

    @Step("Принимаем заказ по ID {orderId} с курьером ID {courierId} через API и проверяем ожидаемый статус {expectedStatusCode}")
    public static void acceptOrderWithExpectedStatusCode(Integer orderId, Integer courierId, int expectedStatusCode) {
        String url;
        if (orderId == null) {
            // Если orderId null, используем фиктивное значение, например 0
            orderId = 0;
        }
        if (courierId == null) {
            // Если courierId null, не добавляем параметр в URL
            url = String.format("/api/v1/orders/accept/%d", orderId);
        } else {
            url = String.format("/api/v1/orders/accept/%d?courierId=%d", orderId, courierId);
        }
        given()
                .put(url)
                .then()
                .statusCode(expectedStatusCode);
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

    @Step("Получаем заказ по трек-номеру {trackNumber} через API и проверяем ожидаемый статус {expectedStatusCode}")
    public static void getOrderByTrackWithExpectedStatusCode(int trackNumber, int expectedStatusCode) {
        given()
                .queryParam("t", trackNumber)
                .get("/api/v1/orders/track")
                .then()
                .statusCode(expectedStatusCode);
    }
}