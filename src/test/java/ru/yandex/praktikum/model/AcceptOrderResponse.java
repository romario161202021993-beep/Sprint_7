package ru.yandex.praktikum.model;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AcceptOrderResponse {
    @SerializedName("ok")
    private Boolean ok;
    @SerializedName("message")
    private String message;
}