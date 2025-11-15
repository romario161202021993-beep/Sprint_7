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
                .statusCode(201) // Ожидаем 201 для успешного создания
                .extract()
                .body()
                .as(CreateCourierResponse.class);
    }

    @Step("Создаём курьера через API и проверяем ожидаемый статус {expectedStatusCode}")
    public static void createCourierWithExpectedStatusCode(Courier courier, int expectedStatusCode) {
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

    @Step("Логинимся как курьер через API и проверяем ожидаемый статус {expectedStatusCode}")
    public static void loginCourierWithExpectedStatusCode(LoginCourier loginData, int expectedStatusCode) {
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
                .statusCode(200) // Ожидаем 200 для успешного удаления
                .extract()
                .body()
                .as(DeleteCourierResponse.class);
    }

    @Step("Удаляем курьера по ID {courierId} через API и проверяем ожидаемый статус {expectedStatusCode}")
    public static void deleteCourierWithExpectedStatusCode(Integer courierId, int expectedStatusCode) {
        if (courierId == null) {
            // Вызов DELETE без ID
            given()
                    .delete("/api/v1/courier/")
                    .then()
                    .statusCode(expectedStatusCode);
        } else {
            given()
                    .delete("/api/v1/courier/{id}", courierId)
                    .then()
                    .statusCode(expectedStatusCode);
        }
    }
}