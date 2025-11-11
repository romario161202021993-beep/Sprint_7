package ru.yandex.praktikum.api;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import ru.yandex.praktikum.model.CreateOrderResponse;
import ru.yandex.praktikum.model.GetOrderByTrackResponse;
import ru.yandex.praktikum.model.Order;
import ru.yandex.praktikum.steps.Steps;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class GetOrderByTrackTest {

    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";

    @Test
    @DisplayName("Успешное получение заказа по трек-номеру")
    public void getOrderByTrackSuccessfully() {
        // 1. Подготовим данные для заказа
        String expectedFirstName = "Анна";
        String expectedLastName = "Аннова";
        String expectedAddress = "Москва, ул. Тестовая, д. 5";
        String expectedMetroStation = "5";
        String expectedPhone = "+79991112233";
        Order order = new Order(expectedFirstName, expectedLastName, expectedAddress, expectedMetroStation, expectedPhone);
        // 2. Создаём заказ
        CreateOrderResponse createResponse = Steps.createOrder(order);
        Integer trackNumber = createResponse.getTrack();
        assertNotNull("Трек-номер должен быть получен из ответа на создание", trackNumber);
        // 3. Получаем заказ по трек-номеру
        GetOrderByTrackResponse response = Steps.getOrderByTrack(trackNumber);
        assertNotNull("Ответ на получение заказа по треку не должен быть null", response);
        Order receivedOrder = response.getOrder();
        assertNotNull("Объект заказа должен быть в ответе", receivedOrder);
        // 4. Проверяем, что все поля совпадают с теми, что мы отправляли при создании
        assertEquals("Имя в заказе должно совпадать с отправленным при создании", expectedFirstName, receivedOrder.getFirstName());
        assertEquals("Фамилия в заказе должна совпадать с отправленной при создании", expectedLastName, receivedOrder.getLastName());
        assertEquals("Адрес в заказе должен совпадать с отправленным при создании", expectedAddress, receivedOrder.getAddress());
        assertEquals("Станция метро в заказе должна совпадать с отправленной при создании", expectedMetroStation, receivedOrder.getMetroStation());
        assertEquals("Телефон в заказе должен совпадать с отправленным при создании", expectedPhone, receivedOrder.getPhone());
        assertEquals("Трек-номер в полученном заказе должен совпадать с трек-номером, по которому мы его искали", trackNumber, receivedOrder.getTrack());
        assertNotNull("ID заказа в полученном объекте не должен быть null", receivedOrder.getId());
    }

    @Test
    @DisplayName("Нельзя получить заказ без трек-номера")
    public void cannotGetOrderByTrackWithoutTrackNumber() {
        given()
                .baseUri(BASE_URL)
                .get("/api/v1/orders/track")
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для поиска"));
    }

    @Test
    @DisplayName("Нельзя получить заказ с несуществующим трек-номером")
    public void cannotGetOrderByTrackWithNonExistentTrackNumber() {
        int nonExistentTrackNumber = 999999;
        given()
                .baseUri(BASE_URL)
                .queryParam("t", nonExistentTrackNumber)
                .get("/api/v1/orders/track")
                .then()
                .statusCode(404); // ОР: Ожидаем 404 Not Found согласно документации
    }
}