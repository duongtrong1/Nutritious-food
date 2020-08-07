package com.spring.dev2chuc.nutritious_food.payload;

import com.spring.dev2chuc.nutritious_food.model.Status;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class SignUpRequest {
//    @NotBlank
//    @Size(min = 4, max = 40)
    private String name;

//    @NotBlank
//    @Size(min = 3, max = 15)
    private String username;

//    @Size(max = 40)
//    @Email
    private String email;

//    @NotBlank
//    @Size(min = 6, max = 20)
    private String password;

//    @NotBlank
//    @Size(max = 20)
    private String phone;
}
