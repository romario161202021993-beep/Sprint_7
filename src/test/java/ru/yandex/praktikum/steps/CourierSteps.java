package ru.yandex.praktikum.steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import ru.yandex.praktikum.model.*;

import static io.restassured.RestAssured.given;

public class CourierSteps {

    @Step("Создаём курьера через API и возвращаем Response")
    public static Response createCourierResponse(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .body(courier)
                .post("/api/v1/courier");
    }

    @Step("Логинимся как курьер через API и возвращаем Response")
    public static Response loginCourierResponse(LoginCourier loginData) {
        return given()
                .header("Content-type", "application/json")
                .body(loginData)
                .post("/api/v1/courier/login");
    }

    @Step("Удаляем курьера по ID {courierId} через API и возвращаем Response")
    public static Response deleteCourierResponse(int courierId) {
        return given()
                .delete("/api/v1/courier/{id}", courierId);
    }

    @Step("Проверяем статус-код {expectedStatusCode} для запроса создания курьера")
    public static ValidatableResponse checkCreateCourierStatusCode(Courier courier, int expectedStatusCode) {
        return createCourierResponse(courier).then().statusCode(expectedStatusCode);
    }

    @Step("Проверяем статус-код {expectedStatusCode} для запроса логина курьера")
    public static ValidatableResponse checkLoginCourierStatusCode(LoginCourier loginData, int expectedStatusCode) {
        return loginCourierResponse(loginData).then().statusCode(expectedStatusCode);
    }

    @Step("Проверяем статус-код {expectedStatusCode} для запроса удаления курьера")
    public static ValidatableResponse checkDeleteCourierStatusCode(int courierId, int expectedStatusCode) {
        return deleteCourierResponse(courierId).then().statusCode(expectedStatusCode);
    }

    // Старые методы, теперь возвращающие объекты, полученные через шаг проверки статуса
    @Step("Создаём курьера через API и проверяем статус-код {expectedStatusCode}")
    public static CreateCourierResponse createCourier(Courier courier, int expectedStatusCode) {
        ValidatableResponse response = checkCreateCourierStatusCode(courier, expectedStatusCode);
        return response.extract().body().as(CreateCourierResponse.class);
    }

    // Упрощенный метод для успешного создания, если нужен только объект ответа
    public static CreateCourierResponse createCourier(Courier courier) {
        // Вызов предыдущего метода с ожидаемым статусом 201
        return createCourier(courier, 201);
    }

    @Step("Логинимся как курьер через API и проверяем статус-код {expectedStatusCode}")
    public static LoginCourierResponse loginCourier(LoginCourier loginData, int expectedStatusCode) {
        ValidatableResponse response = checkLoginCourierStatusCode(loginData, expectedStatusCode);
        return response.extract().body().as(LoginCourierResponse.class);
    }

    // Упрощенный метод для успешного логина, если нужен только объект ответа
    public static LoginCourierResponse loginCourier(LoginCourier loginData) {
        // Вызов предыдущего метода с ожидаемым статусом 200
        return loginCourier(loginData, 200);
    }

    @Step("Удаляем курьера по ID {courierId} через API и проверяем статус-код {expectedStatusCode}")
    public static DeleteCourierResponse deleteCourier(int courierId, int expectedStatusCode) {
        ValidatableResponse response = checkDeleteCourierStatusCode(courierId, expectedStatusCode);
        return response.extract().body().as(DeleteCourierResponse.class);
    }

    // Упрощенный метод для успешного удаления, если нужен только объект ответа
    public static DeleteCourierResponse deleteCourier(int courierId) {
        // Вызов предыдущего метода с ожидаемым статусом 200
        return deleteCourier(courierId, 200);
    }

    // Вспомогательный метод для проверки логина после удаления
    @Step("Проверяем, что курьер с логином {login} не может залогиниться (после удаления), ожидаем код {expectedStatusCode}")
    public static void checkLoginFailsAfterDeletion(String login, String password, int expectedStatusCode) {
        LoginCourier loginData = new LoginCourier(login, password);
        checkLoginCourierStatusCode(loginData, expectedStatusCode);
    }
    // Добавить внутрь класса CourierSteps.java
    @Step("Удаляем курьера без ID в URL через API и возвращаем Response")
    public static Response deleteCourierWithoutIdResponse() {
        return given()
                .delete("/api/v1/courier/");
    }

    @Step("Проверяем статус-код {expectedStatusCode} для запроса удаления курьера без ID в URL")
    public static ValidatableResponse checkDeleteCourierWithoutIdStatusCode(int expectedStatusCode) {
        return deleteCourierWithoutIdResponse().then().statusCode(expectedStatusCode);
    }
}