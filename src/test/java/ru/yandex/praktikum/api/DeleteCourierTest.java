package ru.yandex.praktikum.api;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.BaseTest;
import ru.yandex.praktikum.helpers.DataGenerator;
import ru.yandex.praktikum.model.Courier;
import ru.yandex.praktikum.model.CreateCourierResponse;
import ru.yandex.praktikum.model.DeleteCourierResponse;
import ru.yandex.praktikum.model.LoginCourier;
import ru.yandex.praktikum.steps.CourierSteps;

import static org.junit.Assert.*;

public class DeleteCourierTest extends BaseTest {
    private String testLogin;
    private String testPassword;
    private String testFirstName;
    private Integer createdCourierId;

    @Before
    @DisplayName("Подготовка: создаём курьера для теста удаления")
    public void setUp() {
        testLogin = DataGenerator.getRandomLogin();
        testPassword = DataGenerator.getRandomPassword();
        testFirstName = DataGenerator.getRandomFirstName();
        Courier courier = new Courier(testLogin, testPassword, testFirstName);
        CreateCourierResponse createResponse = CourierSteps.createCourier(courier);
        assertTrue("Курьер должен быть создан", createResponse.getOk());
        this.createdCourierId = CourierSteps.loginCourier(new LoginCourier(testLogin, testPassword)).getId();
        assertNotNull("ID курьера должен быть получен", this.createdCourierId);
    }

    @Test
    @DisplayName("Успешное удаление курьера")
    public void deleteCourierSuccessfully() {
        DeleteCourierResponse response = CourierSteps.deleteCourier(createdCourierId);
        assertNotNull(response);
        assertTrue("Поле 'ok' в ответе должно быть true", response.getOk());
        // Проверяем, что курьер действительно удалён, попробовав залогиниться снова
        // Этот вызов теперь также через Steps
        CourierSteps.loginCourierWithExpectedStatusCode(new LoginCourier(testLogin, testPassword), 404);
    }

    @Test
    @DisplayName("Нельзя удалить курьера без ID")
    public void cannotDeleteCourierWithoutId() {
        // Метод deleteCourierWithExpectedStatusCode теперь обрабатывает ожидаемый статус
        CourierSteps.deleteCourierWithExpectedStatusCode(null, 400);
    }

    @Test
    @DisplayName("Нельзя удалить курьера с несуществующим ID")
    public void cannotDeleteCourierWithNonExistentId() {
        int nonExistentId = 999999;
        // Метод deleteCourierWithExpectedStatusCode теперь обрабатывает ожидаемый статус
        CourierSteps.deleteCourierWithExpectedStatusCode(nonExistentId, 404);
    }

    @org.junit.After
    @DisplayName("Очистка после теста удаления курьера: удаление созданного курьера")
    public void tearDown() {
        if (createdCourierId != null) {
            CourierSteps.deleteCourier(createdCourierId);
        }
    }
}