package com.spring.dev2chuc.nutritious_food.payload.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.io.Serializable;

@Getter
@Setter
public class NutritiousFoodAPIResponse<T> implements Serializable {

    private int status;
    private String message;
    private T body;

    public NutritiousFoodAPIResponse(@Nullable T body, HttpStatus status) {
        Assert.notNull(status, "HttpStatus must not be null");
        Assert.notNull(body, "T body must not be null");
        this.status = status.value();
        this.body = body;
    }

    public NutritiousFoodAPIResponse(String message, HttpStatus status) {
        Assert.notNull(message, "message must not be null");
        Assert.notNull(status, "T body must not be null");
        this.status = status.value();
        this.message = message;
    }
}
