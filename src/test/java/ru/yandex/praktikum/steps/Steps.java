package ru.yandex.praktikum.steps;

import io.qameta.allure.Step;
import ru.yandex.praktikum.model.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class Steps {

    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";

    // --- Courier Steps ---
    @Step("Создаём курьера")
    public static CreateCourierResponse createCourier(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .baseUri(BASE_URL)
                .body(courier)
                .post("/api/v1/courier")
                .then()
                .extract()
                .body()
                .as(CreateCourierResponse.class);
    }

    @Step("Логинимся как курьер")
    public static LoginCourierResponse loginCourier(LoginCourier loginData) {
        return given()
                .header("Content-type", "application/json")
                .baseUri(BASE_URL)
                .body(loginData)
                .post("/api/v1/courier/login")
                .then()
                .extract()
                .body()
                .as(LoginCourierResponse.class);
    }

    @Step("Удаляем курьера по ID {courierId}")
    public static DeleteCourierResponse deleteCourier(int courierId) {
        return given()
                .baseUri(BASE_URL)
                .delete("/api/v1/courier/{id}", courierId)
                .then()
                .extract()
                .body()
                .as(DeleteCourierResponse.class);
    }

    // --- Order Steps ---
    @Step("Создаём заказ")
    public static CreateOrderResponse createOrder(Order order) {
        return given()
                .header("Content-type", "application/json")
                .baseUri(BASE_URL)
                .body(order)
                .post("/api/v1/orders")
                .then()
                .extract()
                .body()
                .as(CreateOrderResponse.class);
    }

    @Step("Получаем список заказов")
    public static OrdersListResponse getOrdersList() {
        return given()
                .baseUri(BASE_URL)
                .get("/api/v1/orders")
                .then()
                .extract()
                .body()
                .as(OrdersListResponse.class);
    }

    @Step("Принимаем заказ по ID {orderId} с курьером ID {courierId}")
    public static AcceptOrderResponse acceptOrder(int orderId, int courierId) {
        return given()
                .baseUri(BASE_URL)
                .put("/api/v1/orders/accept/{id}?courierId={courierId}", orderId, courierId)
                .then()
                .extract()
                .body()
                .as(AcceptOrderResponse.class);
    }

    @Step("Получаем заказ по трек-номеру {trackNumber}")
    public static GetOrderByTrackResponse getOrderByTrack(int trackNumber) {
        return given()
                .baseUri(BASE_URL)
                .queryParam("t", trackNumber)
                .get("/api/v1/orders/track")
                .then()
                .extract()
                .body()
                .as(GetOrderByTrackResponse.class);
    }
}