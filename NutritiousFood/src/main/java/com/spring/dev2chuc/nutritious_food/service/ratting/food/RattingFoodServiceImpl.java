package com.spring.dev2chuc.nutritious_food.service.ratting.food;

import com.spring.dev2chuc.nutritious_food.model.Food;
import com.spring.dev2chuc.nutritious_food.model.RattingFood;
import com.spring.dev2chuc.nutritious_food.model.User;
import com.spring.dev2chuc.nutritious_food.payload.RattingFoodRequest;
import com.spring.dev2chuc.nutritious_food.repository.RattingFoodRepository;
import com.spring.dev2chuc.nutritious_food.service.food.FoodService;
import com.spring.dev2chuc.nutritious_food.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

@Service
public class RattingFoodServiceImpl implements RattingFoodService {

    @Autowired
    RattingFoodRepository rattingFoodRepository;

    @Autowired
    FoodService foodService;

    @Autowired
    UserService userService;

    @Override
    public List<RattingFood> list() {
        return rattingFoodRepository.findAll();
    }

    @Override
    public RattingFood merge(RattingFood rattingFood, RattingFoodRequest rattingFoodRequest) {
        rattingFood.setRate(rattingFoodRequest.getRate());
        rattingFood.setComment(rattingFoodRequest.getComment());
        rattingFood.setImage(rattingFoodRequest.getImage());

        User user = userService.getById(rattingFoodRequest.getUserId());
        if (CollectionUtils.isEmpty(Collections.singleton(user))) {
            throw new RuntimeException("Null pointer exception");
        }

        Food food = foodService.findById(rattingFoodRequest.getFoodId());
        if (food == null) {
            throw new RuntimeException("Null pointer exception");
        }

        rattingFood.setUser(user);
        rattingFood.setFood(food);
        RattingFood result = rattingFoodRepository.save(rattingFood);
        return result;
    }

    @Override
    public RattingFood getDetail(Long id) {
        if (CollectionUtils.isEmpty(Collections.singleton(id))) {
            throw new RuntimeException("Null pointer exception");
        }
        return rattingFoodRepository.findById(id).orElseThrow(null);
    }

    @Override
    public RattingFood update(RattingFood rattingFood, RattingFoodRequest rattingFoodRequest) {
        if (rattingFoodRequest.getRate() != null) rattingFood.setRate(rattingFoodRequest.getRate());
        if (rattingFoodRequest.getComment() != null) rattingFood.setComment(rattingFoodRequest.getComment());
        if (rattingFoodRequest.getImage() != null) rattingFood.setImage(rattingFoodRequest.getImage());

        User user = userService.getById(rattingFoodRequest.getUserId());
        if (user == null) {
            throw new RuntimeException("Null pointer exception");
        }

        Food food = foodService.findById(rattingFoodRequest.getFoodId());
        if (food == null) {
            throw new RuntimeException("Null pointer exception");
        }

        rattingFood.setUser(user);
        rattingFood.setFood(food);
        return rattingFoodRepository.save(rattingFood);
    }
}
