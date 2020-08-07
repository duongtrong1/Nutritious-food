package com.spring.dev2chuc.nutritious_food.model;

import com.spring.dev2chuc.nutritious_food.util.PasswordsEqualConstraint;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@PasswordsEqualConstraint
public class PasswordChange implements Serializable {

    @NotNull
    @NotEmpty(message = "{password.notempty}")
    private String oldPassword;

    @NotNull
    @NotEmpty(message = "{password.notempty}")
    @Size(min = 6, max = 50, message = "{password.size}")
    private String password;

    @NotNull
    @NotEmpty(message = "{confirm.password.notempty}")
    @Size(min = 6, max = 50, message = "{password.size}")
    private String confirmPassword;
//
//    @NotNull
//    @NotEmpty(message = "{email.notempty}")
//    @Size(min = 6, max = 100, message = "{email.size}")
//    private String email;
}
