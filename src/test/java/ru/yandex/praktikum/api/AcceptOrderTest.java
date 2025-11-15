package ru.yandex.praktikum.api;

import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Test;
import ru.yandex.praktikum.BaseTest;
import ru.yandex.praktikum.helpers.DataGenerator;
import ru.yandex.praktikum.model.*;
import ru.yandex.praktikum.steps.CourierSteps;
import ru.yandex.praktikum.steps.OrderSteps;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

public class AcceptOrderTest extends BaseTest {
    private Integer createdCourierId;
    private Integer createdOrderId;

    @Test
    @DisplayName("Успешное принятие заказа")
    public void acceptOrderSuccessfully() {
        // 1. Создаём курьера
        String login = DataGenerator.getRandomLogin();
        String password = DataGenerator.getRandomPassword();
        String firstName = DataGenerator.getRandomFirstName();
        Courier courier = new Courier(login, password, firstName);
        CreateCourierResponse createCourierResponse = CourierSteps.createCourier(courier);
        assertTrue("Курьер должен быть создан", createCourierResponse.getOk());
        Integer courierId = CourierSteps.loginCourier(new LoginCourier(login, password)).getId();
        assertNotNull("ID курьера должен быть получен", courierId);
        this.createdCourierId = courierId;

        // 2. Создаём заказ
        Order order = new Order("Петр", "Петров", "Москва, ул. Тестовая, д. 2", "1", "+79997654321");
        CreateOrderResponse createOrderResponse = OrderSteps.createOrder(order);
        assertNotNull("Трек-номер заказа должен быть в ответе", createOrderResponse.getTrack());
        Integer trackNumber = createOrderResponse.getTrack();
        GetOrderByTrackResponse trackResponse = OrderSteps.getOrderByTrack(trackNumber);
        assertNotNull("Ответ на получение заказа по треку не должен быть null", trackResponse);
        Order foundOrder = trackResponse.getOrder();
        assertNotNull("Объект заказа должен быть в ответе на получение по треку", foundOrder);
        Integer orderId = foundOrder.getId();
        assertNotNull("ID заказа должен быть найден по трек-номеру", orderId);
        this.createdOrderId = orderId;

        // 3. Принимаем заказ по найденному ID
        AcceptOrderResponse acceptResponse = OrderSteps.acceptOrder(orderId, courierId);
        assertNotNull(acceptResponse);
        assertTrue("Поле 'ok' в ответе должно быть true", acceptResponse.getOk());
    }

    @Test
    @DisplayName("Нельзя принять заказ без ID курьера")
    public void cannotAcceptOrderWithoutCourierId() {
        // Создаём курьера и заказ
        String login = DataGenerator.getRandomLogin();
        String password = DataGenerator.getRandomPassword();
        String firstName = DataGenerator.getRandomFirstName();
        Courier courier = new Courier(login, password, firstName);
        CourierSteps.createCourier(courier);
        Integer courierId = CourierSteps.loginCourier(new LoginCourier(login, password)).getId();
        this.createdCourierId = courierId;
        Order order = new Order("Сидор", "Сидоров", "Москва, ул. Тестовая, д. 3", "3", "+79995556677");
        CreateOrderResponse createOrderResponse = OrderSteps.createOrder(order);
        Integer trackNumber = createOrderResponse.getTrack();
        GetOrderByTrackResponse trackResponse = OrderSteps.getOrderByTrack(trackNumber);
        assertNotNull("Ответ на получение заказа по треку не должен быть null", trackResponse);
        Order foundOrder = trackResponse.getOrder();
        assertNotNull("Объект заказа должен быть в ответе на получение по треку", foundOrder);
        Integer orderId = foundOrder.getId();
        assertNotNull("ID заказа должен быть найден по трек-номеру", orderId);
        this.createdOrderId = orderId;

        // Отправляем запрос без courierId в query параметрах
        OrderSteps.acceptOrderWithoutCourierId(orderId, 400);
    }

    @Test
    @DisplayName("Нельзя принять заказ с несуществующим ID курьера")
    public void cannotAcceptOrderWithNonExistentCourierId() {
        // Создаём заказ
        Order order = new Order("Федор", "Федоров", "Москва, ул. Тестовая, д. 4", "4", "+79998889900");
        CreateOrderResponse createOrderResponse = OrderSteps.createOrder(order);
        Integer trackNumber = createOrderResponse.getTrack();
        GetOrderByTrackResponse trackResponse = OrderSteps.getOrderByTrack(trackNumber);
        assertNotNull("Ответ на получение заказа по треку не должен быть null", trackResponse);
        Order foundOrder = trackResponse.getOrder();
        assertNotNull("Объект заказа должен быть в ответе на получение по треку", foundOrder);
        Integer orderId = foundOrder.getId();
        assertNotNull("ID заказа должен быть найден по трек-номеру", orderId);
        this.createdOrderId = orderId;
        int nonExistentCourierId = 999999;

        // Отправляем запрос с несуществующим courierId
        OrderSteps.acceptOrderWithNonExistentCourierId(orderId, nonExistentCourierId, 404);
    }

    @Test
    @DisplayName("Нельзя принять заказ с несуществующим ID заказа")
    public void cannotAcceptOrderWithNonExistentOrderId() {
        // Создаём курьера
        String login = DataGenerator.getRandomLogin();
        String password = DataGenerator.getRandomPassword();
        String firstName = DataGenerator.getRandomFirstName();
        Courier courier = new Courier(login, password, firstName);
        CourierSteps.createCourier(courier);
        Integer courierId = CourierSteps.loginCourier(new LoginCourier(login, password)).getId();
        this.createdCourierId = courierId;
        int nonExistentOrderId = 999999;

        // Отправляем запрос с несуществующим orderId
        OrderSteps.acceptOrderWithNonExistentOrderId(nonExistentOrderId, courierId, 404);
    }

    @After
    @DisplayName("Очистка после теста принятия заказа: удаление созданного курьера")
    public void tearDown() {
        if (createdCourierId != null) {
            CourierSteps.deleteCourier(createdCourierId);
        }
    }
}