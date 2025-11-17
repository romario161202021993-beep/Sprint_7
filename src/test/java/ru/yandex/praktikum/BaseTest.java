package ru.yandex.praktikum;

import io.restassured.RestAssured;
import org.junit.BeforeClass;

public class BaseTest {
    protected static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";

    @BeforeClass
    public static void setUpClass() {
        RestAssured.baseURI = BASE_URL;
    }
}