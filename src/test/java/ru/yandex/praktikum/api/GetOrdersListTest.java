package ru.yandex.praktikum.api;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import ru.yandex.praktikum.BaseTest;
import ru.yandex.praktikum.model.OrdersListResponse;
import ru.yandex.praktikum.steps.OrderSteps;
import static org.junit.Assert.*;

public class GetOrdersListTest extends BaseTest {

    @Test
    @DisplayName("Получение списка заказов")
    public void getOrdersListSuccessfully() {
        OrdersListResponse response = OrderSteps.getOrdersList();
        assertNotNull(response);
        assertNotNull("Список заказов должен быть в ответе", response.getOrders());
    }
}