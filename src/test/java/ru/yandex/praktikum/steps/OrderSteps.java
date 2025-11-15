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

    // Метод для тестирования принятия заказа с ошибками
    @Step("Пытаемся принять заказ с ID {orderId} и курьером ID {courierId}, ожидаем код {expectedStatusCode}")
    public static void acceptOrderExpectingError(int orderId, int courierId, int expectedStatusCode) {
        given()
                .put("/api/v1/orders/accept/{id}?courierId={courierId}", orderId, courierId)
                .then()
                .statusCode(expectedStatusCode);
    }

    // Метод для тестирования принятия заказа без courierId
    @Step("Пытаемся принять заказ с ID {orderId} без указания courierId, ожидаем код {expectedStatusCode}")
    public static void acceptOrderWithoutCourierId(int orderId, int expectedStatusCode) {
        given()
                .put("/api/v1/orders/accept/{id}", orderId)
                .then()
                .statusCode(expectedStatusCode);
    }

    // Метод для тестирования принятия заказа с несуществующим ID заказа
    @Step("Пытаемся принять заказ с несуществующим ID {nonExistentOrderId}, ожидаем код {expectedStatusCode}")
    public static void acceptOrderWithNonExistentOrderId(int nonExistentOrderId, int courierId, int expectedStatusCode) {
        given()
                .put("/api/v1/orders/accept/{id}?courierId={courierId}", nonExistentOrderId, courierId)
                .then()
                .statusCode(expectedStatusCode);
    }

    // Метод для тестирования принятия заказа с несуществующим ID курьера
    @Step("Пытаемся принять заказ с ID {orderId} с несуществующим курьером ID {nonExistentCourierId}, ожидаем код {expectedStatusCode}")
    public static void acceptOrderWithNonExistentCourierId(int orderId, int nonExistentCourierId, int expectedStatusCode) {
        given()
                .put("/api/v1/orders/accept/{id}?courierId={courierId}", orderId, nonExistentCourierId)
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

    // Метод для тестирования получения заказа без трек-номера
    @Step("Пытаемся получить заказ без трек-номера, ожидаем код {expectedStatusCode}")
    public static void getOrderByTrackWithoutNumber(int expectedStatusCode) {
        given()
                .get("/api/v1/orders/track")
                .then()
                .statusCode(expectedStatusCode);
    }

    // Метод для тестирования получения заказа с несуществующим трек-номером
    @Step("Пытаемся получить заказ с несуществующим трек-номером {nonExistentTrackNumber}, ожидаем код {expectedStatusCode}")
    public static void getOrderByTrackWithNonExistentNumber(int nonExistentTrackNumber, int expectedStatusCode) {
        given()
                .queryParam("t", nonExistentTrackNumber)
                .get("/api/v1/orders/track")
                .then()
                .statusCode(expectedStatusCode);
    }
}