package ru.yandex.praktikum.api;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import ru.yandex.praktikum.helpers.DataGenerator;
import ru.yandex.praktikum.model.*;
import ru.yandex.praktikum.steps.Steps;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class AcceptOrderTest {

    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";

    @Test
    @DisplayName("Успешное принятие заказа")
    public void acceptOrderSuccessfully() {
        // 1. Создаём курьера
        String login = DataGenerator.getRandomLogin();
        String password = DataGenerator.getRandomPassword();
        String firstName = DataGenerator.getRandomFirstName();
        Courier courier = new Courier(login, password, firstName);
        CreateCourierResponse createCourierResponse = Steps.createCourier(courier);
        assertTrue("Курьер должен быть создан", createCourierResponse.getOk());
        Integer courierId = Steps.loginCourier(new LoginCourier(login, password)).getId();
        assertNotNull("ID курьера должен быть получен", courierId);

        // 2. Создаём заказ
        Order order = new Order("Петр", "Петров", "Москва, ул. Тестовая, д. 2", "1", "+79997654321");
        CreateOrderResponse createOrderResponse = Steps.createOrder(order);
        assertNotNull("Трек-номер заказа должен быть в ответе", createOrderResponse.getTrack());
        Integer trackNumber = createOrderResponse.getTrack();

        // 3. Получаем ID заказа по его трек-номеру
        GetOrderByTrackResponse trackResponse = Steps.getOrderByTrack(trackNumber);
        assertNotNull("Ответ на получение заказа по треку не должен быть null", trackResponse);
        Order foundOrder = trackResponse.getOrder();
        assertNotNull("Объект заказа должен быть в ответе на получение по треку", foundOrder);
        Integer orderId = foundOrder.getId();
        assertNotNull("ID заказа должен быть найден по трек-номеру", orderId);

        // 4. Принимаем заказ по найденному ID
        AcceptOrderResponse acceptResponse = Steps.acceptOrder(orderId, courierId);
        assertNotNull(acceptResponse);
        assertTrue("Поле 'ok' в ответе должно быть true", acceptResponse.getOk());

        // Удаляем курьера
        Steps.deleteCourier(courierId);
    }

    @Test
    @DisplayName("Нельзя принять заказ без ID курьера")
    public void cannotAcceptOrderWithoutCourierId() {
        // Создаём курьера и заказ
        String login = DataGenerator.getRandomLogin();
        String password = DataGenerator.getRandomPassword();
        String firstName = DataGenerator.getRandomFirstName();
        Courier courier = new Courier(login, password, firstName);
        Steps.createCourier(courier);
        Integer courierId = Steps.loginCourier(new LoginCourier(login, password)).getId();
        Order order = new Order("Сидор", "Сидоров", "Москва, ул. Тестовая, д. 3", "3", "+79995556677");
        CreateOrderResponse createOrderResponse = Steps.createOrder(order);
        Integer trackNumber = createOrderResponse.getTrack();

        GetOrderByTrackResponse trackResponse = Steps.getOrderByTrack(trackNumber);
        assertNotNull("Ответ на получение заказа по треку не должен быть null", trackResponse);
        Order foundOrder = trackResponse.getOrder();
        assertNotNull("Объект заказа должен быть в ответе на получение по треку", foundOrder);
        Integer orderId = foundOrder.getId();
        assertNotNull("ID заказа должен быть найден по трек-номеру", orderId);

        given()
                .baseUri(BASE_URL)
                .put("/api/v1/orders/accept/{id}", orderId) // <-- courierId не передан
                .then()
                .statusCode(400); // ОР: Ожидаем 400 Bad Request согласно документации

        // Удаляем курьера
        Steps.deleteCourier(courierId);
    }


    @Test
    @DisplayName("Нельзя принять заказ с несуществующим ID курьера")
    public void cannotAcceptOrderWithNonExistentCourierId() {
        // Создаём заказ
        Order order = new Order("Федор", "Федоров", "Москва, ул. Тестовая, д. 4", "4", "+79998889900");
        CreateOrderResponse createOrderResponse = Steps.createOrder(order);
        Integer trackNumber = createOrderResponse.getTrack();

        GetOrderByTrackResponse trackResponse = Steps.getOrderByTrack(trackNumber);
        assertNotNull("Ответ на получение заказа по треку не должен быть null", trackResponse);
        Order foundOrder = trackResponse.getOrder();
        assertNotNull("Объект заказа должен быть в ответе на получение по треку", foundOrder);
        Integer orderId = foundOrder.getId();
        assertNotNull("ID заказа должен быть найден по трек-номеру", orderId);

        int nonExistentCourierId = 999999;

        // Отправляем запрос с несуществующим courierId
        given()
                .baseUri(BASE_URL)
                .put("/api/v1/orders/accept/{id}?courierId={courierId}", orderId, nonExistentCourierId)
                .then()
                .statusCode(404);

    }

    @Test
    @DisplayName("Нельзя принять заказ с несуществующим ID заказа")
    public void cannotAcceptOrderWithNonExistentOrderId() {
        // Создаём курьера
        String login = DataGenerator.getRandomLogin();
        String password = DataGenerator.getRandomPassword();
        String firstName = DataGenerator.getRandomFirstName();
        Courier courier = new Courier(login, password, firstName);
        Steps.createCourier(courier);
        Integer courierId = Steps.loginCourier(new LoginCourier(login, password)).getId();

        int nonExistentOrderId = 999999;

        // Отправляем запрос с несуществующим orderId
        given()
                .baseUri(BASE_URL)
                .put("/api/v1/orders/accept/{id}?courierId={courierId}", nonExistentOrderId, courierId)
                .then()
                .statusCode(404);

        // Удаляем курьера
        Steps.deleteCourier(courierId);
    }
}