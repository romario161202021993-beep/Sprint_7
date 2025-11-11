package ru.yandex.praktikum.model;

import com.google.gson.annotations.SerializedName;

public class GetOrderByTrackResponse {

    @SerializedName("order")
    private Order order;

    @SerializedName("message")
    private String message;

    public Order getOrder() {
        return order;
    }

    public String getMessage() {
        return message;
    }
}