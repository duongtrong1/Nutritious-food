package com.spring.dev2chuc.nutritious_food.service.ratting.food;

import com.spring.dev2chuc.nutritious_food.model.RattingFood;
import com.spring.dev2chuc.nutritious_food.payload.RattingFoodRequest;

import java.util.List;

public interface RattingFoodService {

    List<RattingFood> list();

    RattingFood merge(RattingFood rattingFood, RattingFoodRequest rattingFoodRequest);

    RattingFood getDetail(Long id);

    RattingFood update(RattingFood rattingFood, RattingFoodRequest rattingFoodRequest);
}
