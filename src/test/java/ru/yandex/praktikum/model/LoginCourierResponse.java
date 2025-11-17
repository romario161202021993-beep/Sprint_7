package ru.yandex.praktikum.model;

import com.google.gson.annotations.SerializedName;

public class LoginCourierResponse {

    @SerializedName("id")
    private Integer id;

    @SerializedName("message")
    private String message;

    public Integer getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }
}