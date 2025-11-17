package ru.yandex.praktikum.helpers;

import java.util.Random;

public class DataGenerator {

    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static final String FIRST_NAMES = "Александр,Мария,Дмитрий,Елена,Андрей,Ольга,Сергей,Наталья,Иван,Татьяна";
    private static final String[] FIRST_NAMES_ARRAY = FIRST_NAMES.split(",");

    public static String getRandomLogin() {
        Random random = new Random();
        StringBuilder login = new StringBuilder("login");
        for (int i = 0; i < 6; i++) {
            login.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return login.toString();
    }

    public static String getRandomPassword() {
        Random random = new Random();
        StringBuilder password = new StringBuilder("password");
        for (int i = 0; i < 4; i++) {
            password.append(random.nextInt(10));
        }
        return password.toString();
    }

    public static String getRandomFirstName() {
        Random random = new Random();
        return FIRST_NAMES_ARRAY[random.nextInt(FIRST_NAMES_ARRAY.length)];
    }
}