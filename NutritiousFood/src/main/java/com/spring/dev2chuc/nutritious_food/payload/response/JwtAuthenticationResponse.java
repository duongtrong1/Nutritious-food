package com.spring.dev2chuc.nutritious_food.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtAuthenticationResponse {
    private int status;
    private String message;
    private String accessToken;

    public JwtAuthenticationResponse(int status, String message, String accessToken) {
        this.status = status;
        this.message = message;
        String tokenType = "Bearer";
        this.accessToken = tokenType + " " + accessToken;
    }
}
