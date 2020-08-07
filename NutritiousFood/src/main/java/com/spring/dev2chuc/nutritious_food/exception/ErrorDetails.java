package com.spring.dev2chuc.nutritious_food.exception;

import java.io.Serializable;

public class ErrorDetails implements Serializable {
    private int status;
    private String message;

    public ErrorDetails(int status, String message) {
        super();
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
