package ru.yandex.praktikum.api;

import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.BaseTest;
import ru.yandex.praktikum.helpers.DataGenerator;
import ru.yandex.praktikum.model.Courier;
import ru.yandex.praktikum.model.CreateCourierResponse;
import ru.yandex.praktikum.model.LoginCourier;
import ru.yandex.praktikum.steps.CourierSteps;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

public class LoginCourierTest extends BaseTest {
    private String testLogin;
    private String testPassword;
    private String testFirstName;
    private Integer courierId;

    @Before
    @DisplayName("Подготовка: создаём курьера для теста логина")
    public void setUp() {
        // Создаём курьера перед тестами
        testLogin = DataGenerator.getRandomLogin();
        testPassword = DataGenerator.getRandomPassword();
        testFirstName = DataGenerator.getRandomFirstName();
        Courier courier = new Courier(testLogin, testPassword, testFirstName);
        CreateCourierResponse createResponse = CourierSteps.createCourier(courier);
        assertTrue("Курьер должен быть создан", createResponse.getOk());
        // Логинимся, чтобы получить ID для удаления в @After
        this.courierId = CourierSteps.loginCourier(new LoginCourier(testLogin, testPassword)).getId();
    }

    @Test
    @DisplayName("Успешный логин курьера")
    public void loginCourierSuccessfully() {
        LoginCourier loginData = new LoginCourier(testLogin, testPassword);
        ru.yandex.praktikum.model.LoginCourierResponse response = CourierSteps.loginCourier(loginData);
        assertNotNull(response);
        assertNotNull("ID курьера должен быть в ответе", response.getId());
    }

    @Test
    @DisplayName("Нельзя залогиниться без логина")
    public void cannotLoginWithoutLogin() {
        LoginCourier loginData = new LoginCourier(null, testPassword);

        given()
                .header("Content-type", "application/json")
                .body(loginData)
                .post("/api/v1/courier/login")
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Нельзя залогиниться без пароля")
    public void cannotLoginWithoutPassword() {
        LoginCourier loginData = new LoginCourier(testLogin, null);

        given()
                .header("Content-type", "application/json")
                .body(loginData)
                .post("/api/v1/courier/login")
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Нельзя залогиниться с неправильным логином")
    public void cannotLoginWithIncorrectLogin() {
        LoginCourier loginData = new LoginCourier("wrong_login", testPassword);

        given()
                .header("Content-type", "application/json")
                .body(loginData)
                .post("/api/v1/courier/login")
                .then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Нельзя залогиниться с неправильным паролем")
    public void cannotLoginWithIncorrectPassword() {
        LoginCourier loginData = new LoginCourier(testLogin, "wrong_password");

        given()
                .header("Content-type", "application/json")
                .body(loginData)
                .post("/api/v1/courier/login")
                .then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @After
    @DisplayName("Очистка: удаляем созданного курьера")
    public void tearDown() {
        if (courierId != null) {
            CourierSteps.deleteCourier(courierId);
        }
    }
}