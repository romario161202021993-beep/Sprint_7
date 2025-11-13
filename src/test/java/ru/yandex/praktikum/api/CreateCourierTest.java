package ru.yandex.praktikum.api;

import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Test;
import ru.yandex.praktikum.BaseTest;
import ru.yandex.praktikum.helpers.DataGenerator;
import ru.yandex.praktikum.model.Courier;
import ru.yandex.praktikum.model.CreateCourierResponse;
import ru.yandex.praktikum.model.LoginCourier;
import ru.yandex.praktikum.model.LoginCourierResponse;
import ru.yandex.praktikum.steps.CourierSteps;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
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
        this.createdCourierLogin = login; // Сохраняем логин для удаления в @After

        // Логинимся, чтобы получить ID для удаления
        this.createdCourierId = CourierSteps.loginCourier(new LoginCourier(login, password)).getId();

        // Пытаемся создать второго с тем же логином, используя метод из Steps
        CourierSteps.createCourier(secondCourier);

        // Проверяем через прямой вызов API, что возвращается 409
        given()
                .header("Content-type", "application/json")
                .body(secondCourier)
                .post("/api/v1/courier")
                .then()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется"));
    }

    @Test
    @DisplayName("Нельзя создать курьера без логина")
    public void cannotCreateCourierWithoutLogin() {
        Courier courier = new Courier(null, DataGenerator.getRandomPassword(), DataGenerator.getRandomFirstName());

        given()
                .header("Content-type", "application/json")
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
                .body(courier)
                .post("/api/v1/courier")
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Можно создать курьера без firstName (реальное поведение API)")
    public void canCreateCourierWithoutFirstName() {
        Courier courier = new Courier(DataGenerator.getRandomLogin(), DataGenerator.getRandomPassword(), null);
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