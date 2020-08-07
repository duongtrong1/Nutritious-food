package com.spring.dev2chuc.nutritious_food.payload.response;

import com.spring.dev2chuc.nutritious_food.model.UserProfile;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class UserProfileDTO {
    private Long id;
    private Long user_id;
    private Integer height;
    private Integer weight;
    private Integer bodyFat;
    private Double exerciseIntensity;
    private Integer lbmIndex;
    private Integer bmrIndex;
    private Double bmiIndex;
    private Integer tdeeIndex;
    private Integer caloriesConsumed;
    private Integer age;
    private Integer gender;
    private Integer status;
    private Set<CategoryDTO> categories;
    private UserDTO user;

    public UserProfileDTO(UserProfile userProfile, boolean hasCategory, boolean hasUser) {
        this.id = userProfile.getId();
        this.user_id = userProfile.getUser().getId();
        this.height = userProfile.getHeight();
        this.weight = userProfile.getWeight();
        this.bodyFat = userProfile.getBodyFat();
        this.exerciseIntensity = userProfile.getExerciseIntensity();
        this.lbmIndex = userProfile.getLbmIndex();
        this.bmrIndex = userProfile.getBmrIndex();
        this.bmiIndex = userProfile.getBmiIndex();
        this.tdeeIndex = userProfile.getTdeeIndex();
        this.caloriesConsumed = userProfile.getCaloriesConsumed();
        this.age = LocalDate.now().getYear() - userProfile.getYearOfBirth();
        this.gender = userProfile.getGender();
        this.status = userProfile.getStatus();
        if (hasCategory)
            this.categories = userProfile.getCategories().stream().map(x -> new CategoryDTO(x, false, false)).collect(Collectors.toSet());
        if (hasUser) this.user = new UserDTO(userProfile.getUser(), false, false, false, false, false);
    }
}
