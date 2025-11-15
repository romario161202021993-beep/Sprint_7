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
        this.createdCourierLogin = login;

        // Логинимся, чтобы получить ID для удаления
        this.createdCourierId = CourierSteps.loginCourier(new LoginCourier(login, password)).getId();

        // Проверяем, что создание дубликата возвращает ошибку 409
        CourierSteps.createCourierExpectingError(secondCourier, 409);
    }

    @Test
    @DisplayName("Нельзя создать курьера без логина")
    public void cannotCreateCourierWithoutLogin() {
        Courier courier = new Courier(null, DataGenerator.getRandomPassword(), DataGenerator.getRandomFirstName());
        CourierSteps.createCourierWithoutRequiredFields(courier, 400);
    }

    @Test
    @DisplayName("Нельзя создать курьера без пароля")
    public void cannotCreateCourierWithoutPassword() {
        Courier courier = new Courier(DataGenerator.getRandomLogin(), null, DataGenerator.getRandomFirstName());
        CourierSteps.createCourierWithoutRequiredFields(courier, 400);
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