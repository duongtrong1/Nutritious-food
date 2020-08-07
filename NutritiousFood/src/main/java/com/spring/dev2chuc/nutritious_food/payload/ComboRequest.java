package com.spring.dev2chuc.nutritious_food.payload;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Getter
@Setter
public class ComboRequest {

    private String name;
    private String description;
    private String image;
    @Builder.Default
    private float price = 0;
    private float carbonhydrates;
    private float protein;
    private float lipid;
    private float xenluloza;
    private float canxi;
    private float iron;
    private float zinc;
    private float vitaminA;
    private float vitaminB;
    private float vitaminC;
    private float vitaminD;
    private float vitaminE;
    private float calorie;
    private float weight;
    private List<Long> foodIds;
    private List<Long> categoryIds;
}
