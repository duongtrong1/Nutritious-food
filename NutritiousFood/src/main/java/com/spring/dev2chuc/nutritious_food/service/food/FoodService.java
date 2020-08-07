package com.spring.dev2chuc.nutritious_food.service.food;

import com.spring.dev2chuc.nutritious_food.model.Food;
import com.spring.dev2chuc.nutritious_food.model.UserProfile;
import com.spring.dev2chuc.nutritious_food.payload.FoodRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FoodService {

    Food merge(Food food);

    List<Food> findAll();

    Food findById(Long id);

    Food update(Food food, FoodRequest foodRequest);

    Food merge(Food food, FoodRequest foodRequest);

    Food findByStatusAndId(Integer status, Long id);

    List<Food> findAllByIdIn(Collection<Long> ids);

    List<Food> findAllByCategory(Long categoryId);

    Page<Food> foodsWithPaginate(Specification specification, int page, int limit);

    List<Food> suggest(UserProfile userProfile);

    List<Food> suggestByFoodId(Long foodId);

    List<Food> searchByNameAndDescription(String search);
}
