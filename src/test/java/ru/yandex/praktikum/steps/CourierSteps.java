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

    // Метод для тестирования создания дубликата
    @Step("Пытаемся создать дубликат курьера через API, ожидаем код {expectedStatusCode}")
    public static void createCourierExpectingError(Courier courier, int expectedStatusCode) {
        given()
                .header("Content-type", "application/json")
                .body(courier)
                .post("/api/v1/courier")
                .then()
                .statusCode(expectedStatusCode);
    }

    // Метод для тестирования создания курьера без обязательных полей
    @Step("Пытаемся создать курьера без обязательных полей через API, ожидаем код {expectedStatusCode}")
    public static void createCourierWithoutRequiredFields(Courier courier, int expectedStatusCode) {
        given()
                .header("Content-type", "application/json")
                .body(courier)
                .post("/api/v1/courier")
                .then()
                .statusCode(expectedStatusCode);
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

    // Метод для тестирования логина с неправильными данными
    @Step("Пытаемся залогиниться с неправильными данными через API, ожидаем код {expectedStatusCode}")
    public static void loginCourierExpectingError(LoginCourier loginData, int expectedStatusCode) {
        given()
                .header("Content-type", "application/json")
                .body(loginData)
                .post("/api/v1/courier/login")
                .then()
                .statusCode(expectedStatusCode);
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

    // Метод для тестирования удаления курьера без ID
    @Step("Пытаемся удалить курьера без ID через API, ожидаем код {expectedStatusCode}")
    public static void deleteCourierWithoutId(int expectedStatusCode) {
        given()
                .delete("/api/v1/courier/")
                .then()
                .statusCode(expectedStatusCode);
    }

    // Метод для тестирования удаления курьера с несуществующим ID
    @Step("Пытаемся удалить курьера с несуществующим ID {nonExistentId} через API, ожидаем код {expectedStatusCode}")
    public static void deleteCourierWithNonExistentId(int nonExistentId, int expectedStatusCode) {
        given()
                .delete("/api/v1/courier/{id}", nonExistentId)
                .then()
                .statusCode(expectedStatusCode);
    }

    // Метод для проверки логина после удаления
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