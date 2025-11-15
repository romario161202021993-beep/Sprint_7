package ru.yandex.praktikum.steps;

import io.qameta.allure.Step;
import ru.yandex.praktikum.model.*;

import static io.restassured.RestAssured.given;

public class CourierSteps {

    @Step("Создаём курьера через API")
    public static CreateCourierResponse createCourier(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .body(courier)
                .post("/api/v1/courier")
                .then()
                .extract()
                .body()
                .as(CreateCourierResponse.class);
    }

    @Step("Логинимся как курьер через API")
    public static LoginCourierResponse loginCourier(LoginCourier loginData) {
        return given()
                .header("Content-type", "application/json")
                .body(loginData)
                .post("/api/v1/courier/login")
                .then()
                .extract()
                .body()
                .as(LoginCourierResponse.class);
    }

    @Step("Удаляем курьера по ID {courierId} через API")
    public static DeleteCourierResponse deleteCourier(int courierId) {
        return given()
                .delete("/api/v1/courier/{id}", courierId)
                .then()
                .extract()
                .body()
                .as(DeleteCourierResponse.class);
    }

    // Вспомогательный метод для проверки логина после удаления
    @Step("Проверяем, что курьер с логином {login} не может залогиниться (после удаления), ожидаем код {expectedStatusCode}")
    public static void checkLoginFailsAfterDeletion(String login, String password, int expectedStatusCode) {
        LoginCourier loginData = new LoginCourier(login, password);
        given()
                .header("Content-type", "application/json")
                .body(loginData)
                .post("/api/v1/courier/login")
                .then()
                .statusCode(expectedStatusCode);
    }
}