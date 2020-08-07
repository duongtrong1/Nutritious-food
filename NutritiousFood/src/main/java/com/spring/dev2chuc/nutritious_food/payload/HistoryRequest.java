package com.spring.dev2chuc.nutritious_food.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class HistoryRequest {
    private List<Long> foodIds;
    private String comment;
    private int type;
}
