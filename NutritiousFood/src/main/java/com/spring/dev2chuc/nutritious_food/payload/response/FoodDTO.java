package com.spring.dev2chuc.nutritious_food.payload.response;

import com.spring.dev2chuc.nutritious_food.helper.DateTimeHelper;
import com.spring.dev2chuc.nutritious_food.model.Food;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class FoodDTO {
    private Long id;
    private String name;
    private String description;
    private String image;
    private float price;
    private float carbonhydrates;
    private float protein;
    private float lipid;
    private float xenluloza;
    private float canxi;
    private float iron;
    private float zinc;
    private float vitaminA;
    private float vitaminB;
    private float vitaminC;
    private float vitaminD;
    private float vitaminE;
    private float calorie;
    private float weight;
    private int status;
    private String createdAt;
    private String updatedAt;

    private Set<CategoryDTO> categories = new HashSet<>();
    private Set<ComboDTO> combos = new HashSet<>();

    public FoodDTO(Food food, boolean hasCategory, boolean hasCombo) {
        this.id = food.getId();
        this.name = food.getName();
        this.description = food.getDescription();
        this.image = food.getImage();
        this.price = food.getPrice();
        this.carbonhydrates = food.getCarbonhydrates();
        this.protein = food.getProtein();
        this.lipid = food.getLipid();
        this.xenluloza = food.getXenluloza();
        this.canxi = food.getCanxi();
        this.iron = food.getIron();
        this.zinc = food.getZinc();
        this.vitaminA = food.getVitaminA();
        this.vitaminB = food.getVitaminB();
        this.vitaminC = food.getVitaminC();
        this.vitaminD = food.getVitaminD();
        this.vitaminE = food.getVitaminE();
        this.calorie = food.getCalorie();
        this.weight = food.getWeight();
        this.status = food.getStatus();
        this.createdAt = DateTimeHelper.formatDateFromLong(food.getCreatedAt());
        this.updatedAt = DateTimeHelper.formatDateFromLong(food.getUpdatedAt());


        if (hasCategory)
            this.categories = food.getCategories().stream().map(x -> new CategoryDTO(x, false, false)).collect(Collectors.toSet());
        if (hasCombo)
            this.combos = food.getCombos().stream().map(x -> new ComboDTO(x, false, false)).collect(Collectors.toSet());
    }


}
