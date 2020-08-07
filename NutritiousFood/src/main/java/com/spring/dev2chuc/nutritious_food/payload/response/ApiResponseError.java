package com.spring.dev2chuc.nutritious_food.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponseError {
    private int status;
    private String message;

    public ApiResponseError(int status, String message) {
        this.status = status;
        this.message = message;
    }
}


