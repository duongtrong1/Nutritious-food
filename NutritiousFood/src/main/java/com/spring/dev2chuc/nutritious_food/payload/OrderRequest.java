package com.spring.dev2chuc.nutritious_food.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderRequest {
    private Long addressId;
    private Integer type;
    private String note;

    private List<OrderDetailRequest> orderDetails;
}
