package ru.yandex.praktikum.api;

import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Test;
import ru.yandex.praktikum.BaseTest;
import ru.yandex.praktikum.helpers.DataGenerator;
import ru.yandex.praktikum.model.Courier;
import ru.yandex.praktikum.model.CreateCourierResponse;
import ru.yandex.praktikum.model.LoginCourier;
import ru.yandex.praktikum.steps.CourierSteps;

import static org.junit.Assert.*;

public class CreateCourierTest extends BaseTest {
    private Integer createdCourierId;
    private String createdCourierLogin;

    @Test
    @DisplayName("Успешное создание курьера")
    public void createCourierSuccessfully() {
        Courier courier = new Courier(
                DataGenerator.getRandomLogin(),
                DataGenerator.getRandomPassword(),
                DataGenerator.getRandomFirstName()
        );
        // Проверяем статус-код 201 и получаем объект ответа
        CourierSteps.checkCreateCourierStatusCode(courier, 201);
        CreateCourierResponse response = CourierSteps.createCourier(courier);
        assertNotNull(response);
        assertTrue("Поле 'ok' в ответе должно быть true", response.getOk());
    }

    @Test
    @DisplayName("Нельзя создать двух одинаковых курьеров")
    public void cannotCreateDuplicateCourier() {
        String login = DataGenerator.getRandomLogin();
        String password = DataGenerator.getRandomPassword();
        String firstName = DataGenerator.getRandomFirstName();
        Courier firstCourier = new Courier(login, password, firstName);
        Courier secondCourier = new Courier(login, password, firstName);

        // Создаём первого курьера
        CreateCourierResponse firstResponse = CourierSteps.createCourier(firstCourier);
        assertTrue("Первый курьер должен быть создан успешно", firstResponse.getOk());
        this.createdCourierLogin = login;

        // Логинимся, чтобы получить ID для удаления
        this.createdCourierId = CourierSteps.loginCourier(new LoginCourier(login, password)).getId();

        // Проверяем статус-код 409 для второго запроса
        CourierSteps.checkCreateCourierStatusCode(secondCourier, 409);
    }

    @Test
    @DisplayName("Нельзя создать курьера без логина")
    public void cannotCreateCourierWithoutLogin() {
        Courier courier = new Courier(null, DataGenerator.getRandomPassword(), DataGenerator.getRandomFirstName());
        // Проверяем статус-код 400
        CourierSteps.checkCreateCourierStatusCode(courier, 400);
    }

    @Test
    @DisplayName("Нельзя создать курьера без пароля")
    public void cannotCreateCourierWithoutPassword() {
        Courier courier = new Courier(DataGenerator.getRandomLogin(), null, DataGenerator.getRandomFirstName());
        // Проверяем статус-код 400
        CourierSteps.checkCreateCourierStatusCode(courier, 400);
    }

    @Test
    @DisplayName("Можно создать курьера без firstName (реальное поведение API)")
    public void canCreateCourierWithoutFirstName() {
        Courier courier = new Courier(DataGenerator.getRandomLogin(), DataGenerator.getRandomPassword(), null);
        // Проверяем статус-код 201 и получаем объект ответа
        CourierSteps.checkCreateCourierStatusCode(courier, 201);
        CreateCourierResponse response = CourierSteps.createCourier(courier);
        assertNotNull(response);
        assertTrue("Поле 'ok' в ответе должно быть true, так как API позволяет создать курьера без firstName", response.getOk());
    }

    @After
    @DisplayName("Очистка после теста создания курьера: удаление созданного курьера")
    public void tearDown() {
        if (createdCourierId != null) {
            CourierSteps.deleteCourier(createdCourierId);
        }
    }
}