package ru.yandex.praktikum.model;

import com.google.gson.annotations.SerializedName;

public class CreateOrderResponse {

    @SerializedName("track")
    private Integer track;

    @SerializedName("message")
    private String message;

    public Integer getTrack() {
        return track;
    }

    public String getMessage() {
        return message;
    }
}