package com.spring.dev2chuc.nutritious_food.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponsePage<T> {
    private int status;
    private String message;
    private T data;
    private RESTPagination restPagination;

    public ApiResponsePage(int status, String message, T data, RESTPagination restPagination) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.restPagination = restPagination;
    }
}
