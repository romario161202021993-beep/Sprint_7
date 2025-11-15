package ru.yandex.praktikum.api;

import io.qameta.allure.junit4.DisplayName;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.praktikum.BaseTest; // Добавлено наследование от BaseTest
import ru.yandex.praktikum.model.CreateOrderResponse;
import ru.yandex.praktikum.model.Order;
import ru.yandex.praktikum.steps.OrderSteps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class CreateOrderTest extends BaseTest { // Добавлено наследование

    private final List<String> colors;

    public CreateOrderTest(List<String> colors) {
        this.colors = colors;
    }

    @Parameterized.Parameters(name = "Цвета: {0}")
    public static Collection<Object[]> getColors() {
        return Arrays.asList(new Object[][]{
                {Arrays.asList("BLACK")},
                {Arrays.asList("GREY")},
                {Arrays.asList("BLACK", "GREY")},
                {new ArrayList<String>()}
        });
    }

    @org.junit.Test
    @DisplayName("Создание заказа с разными цветами")
    public void createOrderWithDifferentColors() {
        Order order = new Order("Иван", "Иванов", "Москва, ул. Тестовая, д. 1", "4", "+79991234567");
        order.setColor(colors);
        CreateOrderResponse response = OrderSteps.createOrder(order);
        assertNotNull(response);
        assertNotNull("Трек-номер заказа должен быть в ответе", response.getTrack());
    }
}