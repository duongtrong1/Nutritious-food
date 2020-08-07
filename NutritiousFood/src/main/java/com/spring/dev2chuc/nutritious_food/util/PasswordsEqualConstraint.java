package com.spring.dev2chuc.nutritious_food.util;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = PasswordsEqualConstraintValidator.class)
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
public @interface PasswordsEqualConstraint {

    String message() default "{password.not.match}";

    String field() default "{password.not.match}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
