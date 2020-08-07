package com.spring.dev2chuc.nutritious_food.controller;

import com.spring.dev2chuc.nutritious_food.model.*;
import com.spring.dev2chuc.nutritious_food.payload.response.*;
import com.spring.dev2chuc.nutritious_food.service.combo.ComboService;
import com.spring.dev2chuc.nutritious_food.service.food.FoodService;
import com.spring.dev2chuc.nutritious_food.service.schedule.ScheduleService;
import com.spring.dev2chuc.nutritious_food.service.user.UserService;
import com.spring.dev2chuc.nutritious_food.service.userprofile.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/suggest")
public class SuggestController {

    @Autowired
    UserProfileService userProfileService;

    @Autowired
    UserService userService;

    @Autowired
    ComboService comboService;

    @Autowired
    FoodService foodService;

    @Autowired
    ScheduleService scheduleService;

    @GetMapping("/combo")
    public ResponseEntity<Object> suggestCombo() {
        User user = userService.getUserAuth();
        if (user == null) {
            return new ResponseEntity<>(new ApiResponseError(HttpStatus.NOT_FOUND.value(), "User not found"), HttpStatus.NOT_FOUND);
        } else {
            UserProfile userProfile = userProfileService.getLatestByUser(user);
            if (userProfile == null) {
                return new ResponseEntity<> (new ApiResponseCustom<>(HttpStatus.NOT_FOUND.value (), "Profile not found"), HttpStatus.NOT_FOUND);
            }
            List<Combo> combos = comboService.suggest(userProfile);
            return new ResponseEntity<> (new ApiResponseCustom<>(HttpStatus.OK.value (), "Suggest combo success", combos.stream().map(x -> new ComboDTO(x, true, true)).collect(Collectors.toList())), HttpStatus.OK);
        }
    }

    @GetMapping("/food")
    public ResponseEntity<Object> suggestFood() {
        User user = userService.getUserAuth();
        if (user == null) {
            return new ResponseEntity<>(new ApiResponseError(HttpStatus.NOT_FOUND.value(), "User not found"), HttpStatus.NOT_FOUND);
        } else {
            UserProfile userProfile = userProfileService.getLatestByUser(user);
            if (userProfile == null) {
                return new ResponseEntity<> (new ApiResponseCustom<>(HttpStatus.NOT_FOUND.value (), "Profile not found"), HttpStatus.NOT_FOUND);
            }
            List<Food> foods = foodService.suggest(userProfile);
            return new ResponseEntity<> (new ApiResponseCustom<>(HttpStatus.OK.value (), "Suggest food success", foods.stream().map(x -> new FoodDTO(x, true, true)).collect(Collectors.toList())), HttpStatus.OK);
        }
    }

    @GetMapping("/schedule")
    public ResponseEntity<Object> suggestSchedule() {
        User user = userService.getUserAuth();
        if (user == null) {
            return new ResponseEntity<>(new ApiResponseError(HttpStatus.NOT_FOUND.value(), "User not found"), HttpStatus.NOT_FOUND);
        } else {
            UserProfile userProfile = userProfileService.getLatestByUser(user);
            if (userProfile == null) {
                return new ResponseEntity<> (new ApiResponseCustom<>(HttpStatus.NOT_FOUND.value (), "Profile not found"), HttpStatus.NOT_FOUND);
            }
            List<Schedule> schedules = scheduleService.suggest(userProfile);
            return new ResponseEntity<> (new ApiResponseCustom<>(HttpStatus.OK.value (), "Suggest food success", schedules.stream().map(x -> new ScheduleDTO(x, true, true)).collect(Collectors.toList())), HttpStatus.OK);
        }
    }


}
