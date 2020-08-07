package com.spring.dev2chuc.nutritious_food.controller;

import com.spring.dev2chuc.nutritious_food.model.RattingFood;
import com.spring.dev2chuc.nutritious_food.payload.RattingFoodRequest;
import com.spring.dev2chuc.nutritious_food.payload.response.ApiResponseCustom;
import com.spring.dev2chuc.nutritious_food.payload.response.ApiResponseError;
import com.spring.dev2chuc.nutritious_food.service.food.FoodService;
import com.spring.dev2chuc.nutritious_food.service.ratting.food.RattingFoodService;
import com.spring.dev2chuc.nutritious_food.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/ratting-food")
public class RattingFoodController {

    @Autowired
    RattingFoodService rattingFoodService;

    @Autowired
    FoodService foodService;

    @Autowired
    UserService userService;


    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody RattingFoodRequest rattingFoodRequest) {
        RattingFood rattingFood = new RattingFood();
        RattingFood result = rattingFoodService.merge(rattingFood, rattingFoodRequest);
        return new ResponseEntity<>(new ApiResponseCustom<>(HttpStatus.CREATED.value(), "OK", result), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> getList() {
        List<RattingFood> rattingFoods = rattingFoodService.list();
        return new ResponseEntity<>(new ApiResponseCustom<>(HttpStatus.OK.value(), "OK", rattingFoods), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody RattingFoodRequest rattingFoodRequest, @PathVariable Long id) {
        RattingFood rattingFood = rattingFoodService.getDetail(id);
        if (rattingFood == null) {
            return new ResponseEntity<>(new ApiResponseError(HttpStatus.NOT_FOUND.value(), "Ratting food not found"), HttpStatus.NOT_FOUND);
        }

        RattingFood result = rattingFoodService.update(rattingFood, rattingFoodRequest);
        return new ResponseEntity<>(new ApiResponseCustom<>(HttpStatus.OK.value(), "OK", result), HttpStatus.OK);
    }
}