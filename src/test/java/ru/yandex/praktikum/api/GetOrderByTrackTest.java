package ru.yandex.praktikum.api;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import ru.yandex.praktikum.BaseTest;
import ru.yandex.praktikum.model.CreateOrderResponse;
import ru.yandex.praktikum.model.GetOrderByTrackResponse;
import ru.yandex.praktikum.model.Order;
import ru.yandex.praktikum.steps.OrderSteps;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

public class GetOrderByTrackTest extends BaseTest {

    @Test
    @DisplayName("Успешное получение заказа по трек-номеру")
    public void getOrderByTrackSuccessfully() {
        // 1. Подготовим данные для заказа
        String expectedFirstName = "Naruto";
        String expectedLastName = "Uzumaki";
        String expectedAddress = "Kanoha, 142 apt.";
        String expectedMetroStation = "1";
        String expectedPhone = "+7 800 355 35 35";
        Order order = new Order(expectedFirstName, expectedLastName, expectedAddress, expectedMetroStation, expectedPhone);

        // 2. Создаём заказ
        CreateOrderResponse createResponse = OrderSteps.createOrder(order);
        Integer trackNumber = createResponse.getTrack();
        assertNotNull("Трек-номер должен быть получен из ответа на создание", trackNumber);

        // 3. Получаем заказ по трек-номеру
        GetOrderByTrackResponse response = OrderSteps.getOrderByTrack(trackNumber);
        assertNotNull("Ответ на получение заказа по треку не должен быть null", response);
        Order receivedOrder = response.getOrder();
        assertNotNull("Объект заказа должен быть в ответе", receivedOrder);

        // 4. Проверяем, что все поля совпадают с теми, что мы отправляли при создании
        assertEquals("Имя в заказе должно совпадать с отправленным при создании (или с ожидаемым из API)", expectedFirstName, receivedOrder.getFirstName());
        assertEquals("Фамилия в заказе должна совпадать с отправленной при создании (или с ожидаемой из API)", expectedLastName, receivedOrder.getLastName());
        assertEquals("Адрес в заказе должен совпадать с отправленным при создании (или с ожидаемым из API)", expectedAddress, receivedOrder.getAddress());
        assertEquals("Станция метро в заказе должна совпадать с отправленной при создании (или с ожидаемой из API)", expectedMetroStation, receivedOrder.getMetroStation());
        assertEquals("Телефон в заказе должен совпадать с отправленным при создании (или с ожидаемым из API)", expectedPhone, receivedOrder.getPhone());
        assertEquals("Трек-номер в полученном заказе должен совпадать с трек-номером, по которому мы его искали", trackNumber, receivedOrder.getTrack());
        assertNotNull("ID заказа в полученном объекте не должен быть null", receivedOrder.getId());
    }

    @Test
    @DisplayName("Нельзя получить заказ без трек-номера")
    public void cannotGetOrderByTrackWithoutTrackNumber() {
        OrderSteps.getOrderByTrackWithoutNumber(400);
    }

    @Test
    @DisplayName("Нельзя получить заказ с несуществующим трек-номером")
    public void cannotGetOrderByTrackWithNonExistentTrackNumber() {
        int nonExistentTrackNumber = 999999;
        OrderSteps.getOrderByTrackWithNonExistentNumber(nonExistentTrackNumber, 404);
    }
}