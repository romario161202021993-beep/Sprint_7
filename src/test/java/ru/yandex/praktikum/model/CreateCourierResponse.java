package ru.yandex.praktikum.model;

import com.google.gson.annotations.SerializedName;

public class CreateCourierResponse {

    @SerializedName("ok")
    private Boolean ok;

    @SerializedName("message")
    private String message;

    public Boolean getOk() {
        return ok;
    }

    public String getMessage() {
        return message;
    }
}