package com.theost.rxapp;

import java.util.Random;

public class ApiObject {

    private final int id;
    private final String value;
    public ApiObject(int id) {
        Random random = new Random();
        this.id = id;
        this.value = String.valueOf(random.nextInt(1000000000));

    }

    public int getId() {
        return id;
    }

    public String getValue() {
        return value;
    }


}
