package com.spring.dev2chuc.nutritious_food.service.userprofile;

import com.spring.dev2chuc.nutritious_food.model.Gender;
import com.spring.dev2chuc.nutritious_food.model.User;
import com.spring.dev2chuc.nutritious_food.model.UserProfile;
import com.spring.dev2chuc.nutritious_food.payload.UserProfileRequest;
import com.spring.dev2chuc.nutritious_food.repository.UserProfileRepository;
import com.spring.dev2chuc.nutritious_food.service.category.CategoryService;
import com.spring.dev2chuc.nutritious_food.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    @Autowired
    UserProfileRepository userProfileRepository;

    @Autowired
    UserService userService;

    @Autowired
    CategoryService categoryService;

    private static Integer mathCalories(int weight, int height, int age, int gender, double intensity) {
        int index = gender == Gender.MALE.getValue() ? 88362 : 447593;
        int indexWeight = gender == Gender.MALE.getValue() ? 13397 : 9247;
        int indexHeight = gender == Gender.MALE.getValue() ? 4799 : 3098;
        int indexAge = gender == Gender.MALE.getValue() ? 5677 : 4330;
        return (int) ((index + (indexWeight * weight) + (indexHeight * height) - (indexAge * age)) * intensity) / 1000;
    }

    private static Integer mathBmr(int weight, int bodyFat) {
        int index = 370;
        double numberIndex = 21.6;
        return (int) (index + (numberIndex * mathLbm(weight, bodyFat)));
    }

    private static Integer mathLbm(int weight, int bodyFat) {
        int index = 100;
        return (weight * (index - bodyFat)) / index;
    }

    private static double mathBmI(int weight, int height) {
        int index = 100;
        float weightF = (float) weight;
        float heightF = (float) height;
        return (double) Math.round((weightF / ((heightF / index) * (heightF / index))) * 100)/ 100;
    }

    @Override
    public List<UserProfile> getAllByUser(User user) {
        return userProfileRepository.findByUser(user);
    }

    @Override
    public UserProfile store(User user, UserProfileRequest userProfileRequest) {
        Integer calories = mathCalories(userProfileRequest.getWeight(),
                userProfileRequest.getHeight(),
                userProfileRequest.getAge(),
                userProfileRequest.getGender(),
                userProfileRequest.getExerciseIntensity()
        );
        System.out.println(calories);
        UserProfile userProfile = new UserProfile(
                user,
                userProfileRequest.getHeight(),
                userProfileRequest.getWeight(),
                userProfileRequest.getAge(),
                userProfileRequest.getBodyFat(),
                userProfileRequest.getExerciseIntensity(),
                calories,
                calories,
                mathLbm(userProfileRequest.getWeight(), userProfileRequest.getBodyFat()),
                mathBmr(userProfileRequest.getWeight(), userProfileRequest.getBodyFat()),
                mathBmI(userProfileRequest.getWeight(), userProfileRequest.getHeight()),
                userProfileRequest.getGender()
        );
        return userProfileRepository.save(userProfile);
    }

    @Override
    public UserProfile getDetail(Long id) {
        if (CollectionUtils.isEmpty(Collections.singleton(id))) {
            return null;
        }
        System.out.println(id);
        Optional<UserProfile> userProfile = userProfileRepository.findById(id);
        return userProfile.orElse(null);
    }

    @Override
    public UserProfile update(UserProfileRequest userProfileRequest, UserProfile userProfile) {
        if (userProfileRequest.getHeight() != null) userProfile.setHeight(userProfileRequest.getHeight());
        if (userProfileRequest.getWeight() != null) userProfile.setWeight(userProfileRequest.getWeight());
        if (userProfileRequest.getAge() != null)
            userProfile.setYearOfBirth(LocalDate.now().getYear() - userProfileRequest.getHeight());
        if (userProfileRequest.getBodyFat() != null) userProfile.setBodyFat(userProfileRequest.getBodyFat());
        if (userProfileRequest.getExerciseIntensity() != null)
            userProfile.setExerciseIntensity(userProfileRequest.getExerciseIntensity());
        if (userProfileRequest.getCaloriesConsumed() != null)
            userProfile.setCaloriesConsumed(userProfileRequest.getCaloriesConsumed());

        return userProfileRepository.save(userProfile);
    }

    @Override
    public UserProfile updateCategory(UserProfile userProfile) {
        return userProfileRepository.save(userProfile);
    }

    @Override
    public UserProfile getLatestByUser(User user) {
        return userProfileRepository.findTop1ByUserOrderByIdDesc(user);
    }
}
