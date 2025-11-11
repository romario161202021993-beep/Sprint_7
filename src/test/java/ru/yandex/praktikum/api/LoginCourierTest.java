package ru.yandex.praktikum.api;

import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.helpers.DataGenerator;
import ru.yandex.praktikum.model.Courier;
import ru.yandex.praktikum.model.LoginCourier;
import ru.yandex.praktikum.model.LoginCourierResponse;
import ru.yandex.praktikum.steps.Steps;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class LoginCourierTest {

    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";
    private Courier createdCourier;
    private Integer courierId;

    @Before
    @DisplayName("Подготовка: создаём курьера для теста логина")
    public void setUp() {
        // Создаём курьера перед тестами
        String login = DataGenerator.getRandomLogin();
        String password = DataGenerator.getRandomPassword();
        String firstName = DataGenerator.getRandomFirstName();
        createdCourier = new Courier(login, password, firstName);
        Steps.createCourier(createdCourier);
    }

    @Test
    @DisplayName("Успешный логин курьера")
    public void loginCourierSuccessfully() {
        LoginCourier loginData = new LoginCourier(createdCourier.getLogin(), createdCourier.getPassword());
        LoginCourierResponse response = Steps.loginCourier(loginData);
        assertNotNull(response);
        assertNotNull("ID курьера должен быть в ответе", response.getId());
        courierId = response.getId(); // Сохраняем ID для удаления
    }

    @Test
    @DisplayName("Нельзя залогиниться без логина")
    public void cannotLoginWithoutLogin() {
        LoginCourier loginData = new LoginCourier(null, createdCourier.getPassword());
        given()
                .header("Content-type", "application/json")
                .baseUri(BASE_URL)
                .body(loginData)
                .post("/api/v1/courier/login")
                .then()
                .statusCode(400) // ОР: Ожидаем 400 Bad Request согласно документации
                .body("message", equalTo("Недостаточно данных для входа")); // ОР: Ожидаем сообщение
        // Тест упадет, если API возвращает 400 без тела
    }

    @Test
    @DisplayName("Нельзя залогиниться без пароля")
    public void cannotLoginWithoutPassword() {
        LoginCourier loginData = new LoginCourier(createdCourier.getLogin(), null);
        given()
                .header("Content-type", "application/json")
                .baseUri(BASE_URL)
                .body(loginData)
                .post("/api/v1/courier/login")
                .then()
                .statusCode(400) // ОР: Ожидаем 400 Bad Request согласно документации
                .body("message", equalTo("Недостаточно данных для входа")); // ОР: Ожидаем сообщение
        // Тест упадет, если API возвращает 400 без тела
    }

    @Test
    @DisplayName("Нельзя залогиниться с неправильным логином")
    public void cannotLoginWithIncorrectLogin() {
        LoginCourier loginData = new LoginCourier("wrong_login", createdCourier.getPassword());
        given()
                .header("Content-type", "application/json")
                .baseUri(BASE_URL)
                .body(loginData)
                .post("/api/v1/courier/login")
                .then()
                .statusCode(404) // ОР: Ожидаем 404 Not Found согласно документации
                .body("message", equalTo("Учетная запись не найдена")); // ОР: Ожидаем сообщение
        // Тест упадет, если API возвращает 404 без тела
    }

    @Test
    @DisplayName("Нельзя залогиниться с неправильным паролем")
    public void cannotLoginWithIncorrectPassword() {
        LoginCourier loginData = new LoginCourier(createdCourier.getLogin(), "wrong_password");
        given()
                .header("Content-type", "application/json")
                .baseUri(BASE_URL)
                .body(loginData)
                .post("/api/v1/courier/login")
                .then()
                .statusCode(404) // ОР: Ожидаем 404 Not Found согласно документации
                .body("message", equalTo("Учетная запись не найдена")); // ОР: Ожидаем сообщение
    }

    @After
    @DisplayName("Очистка: удаляем созданного курьера")
    public void tearDown() {
        if (courierId != null) {
            Steps.deleteCourier(courierId);
        }
    }
}