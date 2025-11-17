package ru.yandex.praktikum.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class OrdersListResponse {

    @SerializedName("orders")
    private List<Order> orders;

    @SerializedName("message")
    private String message;

    public List<Order> getOrders() {
        return orders;
    }

    public String getMessage() {
        return message;
    }
}