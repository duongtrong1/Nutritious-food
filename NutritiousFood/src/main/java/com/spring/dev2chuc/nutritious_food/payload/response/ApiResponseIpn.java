package com.spring.dev2chuc.nutritious_food.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ApiResponseIpn<T> implements Serializable {
    private String RspCode;
    private String Message;

    public ApiResponseIpn(String rspCode, String message) {
        RspCode = rspCode;
        Message = message;
    }
}
