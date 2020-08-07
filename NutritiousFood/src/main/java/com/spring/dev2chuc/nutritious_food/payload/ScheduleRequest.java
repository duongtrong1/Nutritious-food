package com.spring.dev2chuc.nutritious_food.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ScheduleRequest {
    private String name;
    private String description;
    private float price;
    private String image;
    private List<Long> categoryIds;
}
