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

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

public class DeleteCourierTest extends BaseTest {
    private String testLogin;
    private String testPassword;
    private String testFirstName;
    private Integer createdCourierId;

    @Before
    @DisplayName("Подготовка: создаём курьера для теста удаления")
    public void setUpCourierForDeleteTest() {
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
        // Проверяем статус-код 200 и получаем объект ответа
        CourierSteps.checkDeleteCourierStatusCode(createdCourierId, 200);
        DeleteCourierResponse response = CourierSteps.deleteCourier(createdCourierId);
        assertNotNull(response);
        assertTrue("Поле 'ok' в ответе должно быть true", response.getOk());

        // Проверяем, что курьер действительно удалён, попробовав залогиниться снова
        CourierSteps.checkLoginCourierStatusCode(new LoginCourier(testLogin, testPassword), 404);
    }

    @Test
    @DisplayName("Нельзя удалить курьера без ID")
    public void cannotDeleteCourierWithoutId() {
        CourierSteps.checkDeleteCourierWithoutIdStatusCode(400); // Ожидаем 400, если документация указывает на это, иначе 404
    }

    @Test
    @DisplayName("Нельзя удалить курьера с несуществующим ID")
    public void cannotDeleteCourierWithNonExistentId() {
        int nonExistentId = 999999;
        CourierSteps.checkDeleteCourierStatusCode(nonExistentId, 404);
    }

    @org.junit.After
    @DisplayName("Очистка после теста удаления курьера: удаление созданного курьера")
    public void tearDown() {
        if (createdCourierId != null) {
            CourierSteps.deleteCourier(createdCourierId);
        }
    }
}