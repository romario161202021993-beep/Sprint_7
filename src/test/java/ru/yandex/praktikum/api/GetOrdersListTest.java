package ru.yandex.praktikum.api;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import ru.yandex.praktikum.model.OrdersListResponse;
import ru.yandex.praktikum.steps.Steps;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class GetOrdersListTest {

    @Test
    @DisplayName("Получение списка заказов")
    public void getOrdersListSuccessfully() {
        OrdersListResponse response = Steps.getOrdersList();

        assertNotNull(response);
        assertNotNull("Список заказов должен быть в ответе", response.getOrders());
    }
}