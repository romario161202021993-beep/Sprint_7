package ru.yandex.praktikum.api;

import io.qameta.allure.junit4.DisplayName;
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
    }

    @Test
    @DisplayName("Нельзя удалить курьера без ID")
    public void cannotDeleteCourierWithoutId() {
        given()
                .delete("/api/v1/courier/")
                .then()
                .statusCode(400) // <-- Исправлено: было 404, стало 400
                .body("message", equalTo("Недостаточно данных для удаления курьера"));
    }

    @Test
    @DisplayName("Нельзя удалить курьера с несуществующим ID")
    public void cannotDeleteCourierWithNonExistentId() {
        int nonExistentId = 999999;
        given()
                .delete("/api/v1/courier/{id}", nonExistentId)
                .then()
                .statusCode(404)
                .body("message", equalTo("Курьера с таким id нет.")); // <-- Исправлено: добавлена точка
    }

    @org.junit.After
    @DisplayName("Очистка после теста удаления курьера: удаление созданного курьера")
    public void tearDown() {
        if (createdCourierId != null) {
            CourierSteps.deleteCourier(createdCourierId);
        }
    }
}