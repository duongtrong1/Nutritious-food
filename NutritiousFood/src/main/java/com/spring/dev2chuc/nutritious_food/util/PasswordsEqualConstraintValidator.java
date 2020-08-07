package com.spring.dev2chuc.nutritious_food.util;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Method;

public class PasswordsEqualConstraintValidator implements
        ConstraintValidator<PasswordsEqualConstraint, Object> {

    @Override
    public void initialize(PasswordsEqualConstraint arg0) {
    }

    @Override
    public boolean isValid(Object candidate, ConstraintValidatorContext context) {
        try {
            Method methodGetPassword = candidate.getClass().getMethod("getPassword");
            Method methodGetConfirmPassword = candidate.getClass().getMethod("getConfirmPassword");
            if (methodGetPassword.invoke(candidate) == null && methodGetConfirmPassword.invoke(candidate) == null)
                return true;
            else if (methodGetPassword.invoke(candidate) == null)
                return false;
            return methodGetPassword.invoke(candidate).equals(methodGetConfirmPassword.invoke(candidate));
        } catch (NoSuchMethodException noSuchMethodException) {
            noSuchMethodException.printStackTrace();
            return false;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }
}
