package com.spring.dev2chuc.nutritious_food.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDetailRequest {
    private Long foodId;
    private Long comboId;
    private Long scheduleId;
    private int quantity;
}
