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
        CourierSteps.loginCourierExpectingError(loginData, 400);
    }

    @Test
    @DisplayName("Нельзя залогиниться без пароля")
    public void cannotLoginWithoutPassword() {
        LoginCourier loginData = new LoginCourier(testLogin, null);
        CourierSteps.loginCourierExpectingError(loginData, 400);
    }

    @Test
    @DisplayName("Нельзя залогиниться с неправильным логином")
    public void cannotLoginWithIncorrectLogin() {
        LoginCourier loginData = new LoginCourier("wrong_login", testPassword);
        CourierSteps.loginCourierExpectingError(loginData, 404);
    }

    @Test
    @DisplayName("Нельзя залогиниться с неправильным паролем")
    public void cannotLoginWithIncorrectPassword() {
        LoginCourier loginData = new LoginCourier(testLogin, "wrong_password");
        CourierSteps.loginCourierExpectingError(loginData, 404);
    }

    @After
    @DisplayName("Очистка: удаляем созданного курьера")
    public void tearDown() {
        if (courierId != null) {
            CourierSteps.deleteCourier(courierId);
        }
    }
}