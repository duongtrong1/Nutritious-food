package com.spring.dev2chuc.nutritious_food.payload;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
public class UserProfileRequest {
    @NotNull
    private Integer height;
    @NotNull
    private Integer weight;
    @NotNull
    private Integer age;
    @NotNull
    private Integer gender;
    @NotNull
    private Integer bodyFat;
    @NotNull
    private Double exerciseIntensity;
    private Integer caloriesConsumed;
    private Integer status;
    private Set<Long> categoryIds;
}
