package ru.yandex.praktikum.api;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import ru.yandex.praktikum.helpers.DataGenerator;
import ru.yandex.praktikum.model.Courier;
import ru.yandex.praktikum.model.CreateCourierResponse;
import ru.yandex.praktikum.model.DeleteCourierResponse;
import ru.yandex.praktikum.model.LoginCourier;
import ru.yandex.praktikum.steps.Steps;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class DeleteCourierTest {

    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";

    @Test
    @DisplayName("Успешное удаление курьера")
    public void deleteCourierSuccessfully() {
        // 1. Создаём курьера
        String login = DataGenerator.getRandomLogin();
        String password = DataGenerator.getRandomPassword();
        String firstName = DataGenerator.getRandomFirstName();
        Courier courier = new Courier(login, password, firstName);
        CreateCourierResponse createResponse = Steps.createCourier(courier);
        assertTrue("Курьер должен быть создан", createResponse.getOk());
        // 2. Логинимся, чтобы получить ID
        LoginCourier loginData = new LoginCourier(login, password);
        Integer courierId = Steps.loginCourier(loginData).getId();
        assertNotNull("ID курьера должен быть получен", courierId);
        // 3. Удаляем курьера по ID
        DeleteCourierResponse deleteResponse = Steps.deleteCourier(courierId);
        assertNotNull(deleteResponse);
        assertTrue("Поле 'ok' в ответе должно быть true", deleteResponse.getOk());
    }

    @Test
    @DisplayName("Нельзя удалить курьера без ID")
    public void cannotDeleteCourierWithoutId() {
        given()
                .baseUri(BASE_URL)
                .delete("/api/v1/courier/")
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для удаления курьера"));
    }

    @Test
    @DisplayName("Нельзя удалить курьера с несуществующим ID")
    public void cannotDeleteCourierWithNonExistentId() {
        int nonExistentId = 999999;
        given()
                .baseUri(BASE_URL)
                .delete("/api/v1/courier/{id}", nonExistentId)
                .then()
                .statusCode(400)
                .body("message", equalTo("Курьер не найден"));
    }
}