package com.spring.dev2chuc.nutritious_food.controller;

import com.spring.dev2chuc.nutritious_food.model.*;
import com.spring.dev2chuc.nutritious_food.payload.UserProfileRequest;
import com.spring.dev2chuc.nutritious_food.payload.response.ApiResponseCustom;
import com.spring.dev2chuc.nutritious_food.payload.response.ApiResponseError;
import com.spring.dev2chuc.nutritious_food.payload.response.UserProfileDTO;
import com.spring.dev2chuc.nutritious_food.service.category.CategoryService;
import com.spring.dev2chuc.nutritious_food.service.user.UserService;
import com.spring.dev2chuc.nutritious_food.service.userprofile.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/user-profile")
public class UserProfileController {

    @Autowired
    UserProfileService userProfileService;

    @Autowired
    UserService userService;

    @Autowired
    CategoryService categoryService;

    @GetMapping()
    public ResponseEntity<?> getList() {
        User user = userService.getUserAuth();
        if (user == null) {
            return new ResponseEntity<>(new ApiResponseError(HttpStatus.NOT_FOUND.value(), "User not found"), HttpStatus.NOT_FOUND);
        } else {
            List<UserProfile> userProfiles = userProfileService.getAllByUser(user);
            return new ResponseEntity<>(new ApiResponseCustom<>(HttpStatus.OK.value(), "Get List user profile success", userProfiles.stream().map(x -> new UserProfileDTO(x, true, false))), HttpStatus.OK);
        }
    }

    @GetMapping("/latest")
    public ResponseEntity<?> getLatest() {
        User user = userService.getUserAuth();
        if (user == null) {
            return new ResponseEntity<>(new ApiResponseError(HttpStatus.NOT_FOUND.value(), "User not found"), HttpStatus.NOT_FOUND);
        } else {
            UserProfile userProfile = userProfileService.getLatestByUser(user);
            if (userProfile == null) {
                return new ResponseEntity<>(new ApiResponseError(HttpStatus.NOT_FOUND.value(), "User don't has any profile"), HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(new ApiResponseCustom<>(HttpStatus.OK.value(), "Get List user profile success", new UserProfileDTO(userProfile, true, true)), HttpStatus.OK);
        }
    }


    @PostMapping("/create")
    public ResponseEntity<Object> createUserProfile(@Valid @RequestBody UserProfileRequest userProfileRequest) {
        User user = userService.getUserAuth();
        if (user == null) {
            return new ResponseEntity<>(new ApiResponseError(HttpStatus.NOT_FOUND.value(), "User not found"), HttpStatus.NOT_FOUND);
        } else {
            if (userProfileRequest.getGender() != null && !Gender.hasValue(userProfileRequest.getGender())) {
                return new ResponseEntity<>(new ApiResponseError(HttpStatus.NOT_FOUND.value(), "Gender not found"), HttpStatus.NOT_FOUND);
            }
            if (userProfileRequest.getExerciseIntensity() != null && !ExerciseIntensity.hasValue(userProfileRequest.getExerciseIntensity())) {
                return new ResponseEntity<>(new ApiResponseError(HttpStatus.NOT_FOUND.value(), "ExerciseIntensity not found"), HttpStatus.NOT_FOUND);
            }
            UserProfile profile = userProfileService.store(user, userProfileRequest);
            return new ResponseEntity<>(new ApiResponseCustom<>(HttpStatus.CREATED.value(), "Save user profile success", new UserProfileDTO(profile, false, true)), HttpStatus.CREATED);
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@Valid @RequestBody UserProfileRequest userProfileRequest, @PathVariable("id") Long id) {
        User user = userService.getUserAuth();
        if (user == null) {
            return new ResponseEntity<>(new ApiResponseError(HttpStatus.NOT_FOUND.value(), "User not found"), HttpStatus.NOT_FOUND);
        } else {
            if (userProfileRequest.getGender() != null && !Gender.hasValue(userProfileRequest.getGender())) {
                return new ResponseEntity<>(new ApiResponseError(HttpStatus.NOT_FOUND.value(), "Gender not found"), HttpStatus.NOT_FOUND);
            }

            if (userProfileRequest.getExerciseIntensity() != null && !ExerciseIntensity.hasValue(userProfileRequest.getExerciseIntensity())) {
                return new ResponseEntity<>(new ApiResponseError(HttpStatus.NOT_FOUND.value(), "ExerciseIntensity not found"), HttpStatus.NOT_FOUND);
            }
            UserProfile profile = userProfileService.getDetail(id);
            if (profile == null)
                return new ResponseEntity<>(new ApiResponseCustom<>(HttpStatus.NOT_FOUND.value(), "Profile not found"), HttpStatus.NOT_FOUND);

            UserProfile result = userProfileService.update(userProfileRequest, profile);
            return new ResponseEntity<>(new ApiResponseCustom<>(HttpStatus.OK.value(), "Update user profile success", new UserProfileDTO(result, true, true)), HttpStatus.OK);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable("id") Long id) {
        System.out.println(id);
        UserProfile userProfile = userProfileService.getDetail(id);
        if (userProfile == null)
            return new ResponseEntity<>(new ApiResponseError(HttpStatus.NOT_FOUND.value(), "Profile not found"), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(new ApiResponseCustom<>(HttpStatus.OK.value(), "Get detail success", new UserProfileDTO(userProfile, true, true)), HttpStatus.OK);
    }

    @PutMapping("/{id}/category")
    public ResponseEntity<?> updateCategory(@RequestBody List<Long> ids, @PathVariable("id") Long id) {
        UserProfile userProfile = userProfileService.getDetail(id);
        if (userProfile == null) {
            return new ResponseEntity<>(new ApiResponseError(HttpStatus.NOT_FOUND.value(), "Id not found"), HttpStatus.NOT_FOUND);
        }
        List<Category> categories = categoryService.findAllByIdIn(ids);
        Set<Category> categorySet = new HashSet<>(categories);
        userProfile.setCategories(categorySet);
        UserProfile profile = userProfileService.updateCategory(userProfile);
        return new ResponseEntity<>(new ApiResponseCustom<>(HttpStatus.OK.value(), "Merge new category success", new UserProfileDTO(profile, true, true)), HttpStatus.OK);
    }
}
