package ru.yandex.praktikum.api;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import ru.yandex.praktikum.helpers.DataGenerator;
import ru.yandex.praktikum.model.Courier;
import ru.yandex.praktikum.model.CreateCourierResponse;
import ru.yandex.praktikum.steps.Steps;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class CreateCourierTest {

    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";

    @Test
    @DisplayName("Успешное создание курьера")
    public void createCourierSuccessfully() {
        Courier courier = new Courier(
                DataGenerator.getRandomLogin(),
                DataGenerator.getRandomPassword(),
                DataGenerator.getRandomFirstName()
        );
        CreateCourierResponse response = Steps.createCourier(courier);
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
        CreateCourierResponse firstResponse = Steps.createCourier(firstCourier);
        assertTrue("Первый курьер должен быть создан успешно", firstResponse.getOk());

        // Пытаемся создать второго с тем же логином
        given()
                .header("Content-type", "application/json")
                .baseUri(BASE_URL)
                .body(secondCourier)
                .post("/api/v1/courier")
                .then()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Нельзя создать курьера без логина")
    public void cannotCreateCourierWithoutLogin() {
        Courier courier = new Courier(null, DataGenerator.getRandomPassword(), DataGenerator.getRandomFirstName());
        given()
                .header("Content-type", "application/json")
                .baseUri(BASE_URL)
                .body(courier)
                .post("/api/v1/courier")
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Нельзя создать курьера без пароля")
    public void cannotCreateCourierWithoutPassword() {
        Courier courier = new Courier(DataGenerator.getRandomLogin(), null, DataGenerator.getRandomFirstName());
        given()
                .header("Content-type", "application/json")
                .baseUri(BASE_URL)
                .body(courier)
                .post("/api/v1/courier")
                .then()
                .statusCode(400) // ОР: Ожидаем 400 Bad Request
                .body("message", equalTo("Недостаточно данных для создания учетной записи")); // ОР: Ожидаем сообщение
        // Тест упадет, если API возвращает 400 без тела
    }

    @Test
    @DisplayName("Можно создать курьера без firstName (реальное поведение API)")
    public void canCreateCourierWithoutFirstName() {
        Courier courier = new Courier(DataGenerator.getRandomLogin(), DataGenerator.getRandomPassword(), null);
        CreateCourierResponse response = Steps.createCourier(courier);
        assertNotNull(response);
        assertTrue("Поле 'ok' в ответе должно быть true, так как API позволяет создать курьера без firstName", response.getOk());
    }
}