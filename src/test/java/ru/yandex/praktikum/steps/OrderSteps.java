package ru.yandex.praktikum.steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import ru.yandex.praktikum.model.*;

import static io.restassured.RestAssured.given;

public class OrderSteps {

    @Step("Создаём заказ через API и возвращаем Response")
    public static Response createOrderResponse(Order order) {
        return given()
                .header("Content-type", "application/json")
                .body(order)
                .post("/api/v1/orders");
    }

    @Step("Получаем список заказов через API и возвращаем Response")
    public static Response getOrdersListResponse() {
        return given()
                .get("/api/v1/orders");
    }

    @Step("Принимаем заказ по ID {orderId} с курьером ID {courierId} через API и возвращаем Response")
    public static Response acceptOrderResponse(int orderId, int courierId) {
        return given()
                .put("/api/v1/orders/accept/{id}?courierId={courierId}", orderId, courierId);
    }

    @Step("Получаем заказ по трек-номеру {trackNumber} через API и возвращаем Response")
    public static Response getOrderByTrackResponse(int trackNumber) {
        return given()
                .queryParam("t", trackNumber)
                .get("/api/v1/orders/track");
    }

    @Step("Проверяем статус-код {expectedStatusCode} для запроса создания заказа")
    public static ValidatableResponse checkCreateOrderStatusCode(Order order, int expectedStatusCode) {
        return createOrderResponse(order).then().statusCode(expectedStatusCode);
    }

    @Step("Проверяем статус-код {expectedStatusCode} для запроса получения списка заказов")
    public static ValidatableResponse checkGetOrdersListStatusCode(int expectedStatusCode) {
        return getOrdersListResponse().then().statusCode(expectedStatusCode);
    }

    @Step("Проверяем статус-код {expectedStatusCode} для запроса принятия заказа")
    public static ValidatableResponse checkAcceptOrderStatusCode(int orderId, int courierId, int expectedStatusCode) {
        return acceptOrderResponse(orderId, courierId).then().statusCode(expectedStatusCode);
    }

    @Step("Проверяем статус-код {expectedStatusCode} для запроса получения заказа по треку")
    public static ValidatableResponse checkGetOrderByTrackStatusCode(int trackNumber, int expectedStatusCode) {
        return getOrderByTrackResponse(trackNumber).then().statusCode(expectedStatusCode);
    }

    // Старые методы, теперь возвращающие объекты, полученные через шаг проверки статуса
    @Step("Создаём заказ через API и проверяем статус-код {expectedStatusCode}")
    public static CreateOrderResponse createOrder(Order order, int expectedStatusCode) {
        ValidatableResponse response = checkCreateOrderStatusCode(order, expectedStatusCode);
        return response.extract().body().as(CreateOrderResponse.class);
    }

    // Упрощенный метод для успешного создания, если нужен только объект ответа
    public static CreateOrderResponse createOrder(Order order) {
        // Вызов предыдущего метода с ожидаемым статусом 201
        return createOrder(order, 201);
    }

    @Step("Получаем список заказов через API и проверяем статус-код {expectedStatusCode}")
    public static OrdersListResponse getOrdersList(int expectedStatusCode) {
        ValidatableResponse response = checkGetOrdersListStatusCode(expectedStatusCode);
        return response.extract().body().as(OrdersListResponse.class);
    }

    // Упрощенный метод для получения списка, если нужен только объект ответа
    public static OrdersListResponse getOrdersList() {
        // Вызов предыдущего метода с ожидаемым статусом 200
        return getOrdersList(200);
    }

    @Step("Принимаем заказ по ID {orderId} с курьером ID {courierId} через API и проверяем статус-код {expectedStatusCode}")
    public static AcceptOrderResponse acceptOrder(int orderId, int courierId, int expectedStatusCode) {
        ValidatableResponse response = checkAcceptOrderStatusCode(orderId, courierId, expectedStatusCode);
        return response.extract().body().as(AcceptOrderResponse.class);
    }

    // Упрощенный метод для успешного принятия, если нужен только объект ответа
    public static AcceptOrderResponse acceptOrder(int orderId, int courierId) {
        // Вызов предыдущего метода с ожидаемым статусом 200
        return acceptOrder(orderId, courierId, 200);
    }

    @Step("Получаем заказ по трек-номеру {trackNumber} через API и проверяем статус-код {expectedStatusCode}")
    public static GetOrderByTrackResponse getOrderByTrack(int trackNumber, int expectedStatusCode) {
        ValidatableResponse response = checkGetOrderByTrackStatusCode(trackNumber, expectedStatusCode);
        return response.extract().body().as(GetOrderByTrackResponse.class);
    }

    // Упрощенный метод для получения заказа по треку, если нужен только объект ответа
    public static GetOrderByTrackResponse getOrderByTrack(int trackNumber) {
        // Вызов предыдущего метода с ожидаемым статусом 200
        return getOrderByTrack(trackNumber, 200);
    }
    // Добавить внутрь класса OrderSteps.java
    @Step("Принимаем заказ по ID {orderId} без указания courierId через API и возвращаем Response")
    public static Response acceptOrderWithoutCourierIdResponse(int orderId) {
        return given()
                .put("/api/v1/orders/accept/{id}", orderId);
    }

    @Step("Проверяем статус-код {expectedStatusCode} для запроса принятия заказа без courierId")
    public static ValidatableResponse checkAcceptOrderWithoutCourierIdStatusCode(int orderId, int expectedStatusCode) {
        return acceptOrderWithoutCourierIdResponse(orderId).then().statusCode(expectedStatusCode);
    }

    @Step("Получаем заказ по трек-номеру без указания параметра t через API и возвращаем Response")
    public static Response getOrderByTrackWithoutTrackNumberResponse() {
        return given()
                .get("/api/v1/orders/track");
    }

    @Step("Проверяем статус-код {expectedStatusCode} для запроса получения заказа по треку без параметра t")
    public static ValidatableResponse checkGetOrderByTrackStatusCodeWithoutTrackNumber(int expectedStatusCode) {
        return getOrderByTrackWithoutTrackNumberResponse().then().statusCode(expectedStatusCode);
    }
}