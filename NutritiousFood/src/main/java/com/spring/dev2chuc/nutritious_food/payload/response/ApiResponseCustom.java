package com.spring.dev2chuc.nutritious_food.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ApiResponseCustom<T> implements Serializable {
    private int status;
    private String message;
    private T data;

    public ApiResponseCustom(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public ApiResponseCustom(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
