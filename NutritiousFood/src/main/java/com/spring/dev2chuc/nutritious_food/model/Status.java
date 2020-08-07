package com.spring.dev2chuc.nutritious_food.model;

public enum Status {
    CONFIRM(2),
    ACTIVE(1),
    DEACTIVE(0);

    private Integer value;

    Status(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
