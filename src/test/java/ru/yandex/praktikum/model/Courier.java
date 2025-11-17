package ru.yandex.praktikum.model;

import com.google.gson.annotations.SerializedName;

public class Courier {

    @SerializedName("login")
    private String login;

    @SerializedName("password")
    private String password;

    @SerializedName("firstName")
    private String firstName;

    public Courier() {
    }

    public Courier(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}