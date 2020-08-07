package com.spring.dev2chuc.nutritious_food.payload.response;

import com.spring.dev2chuc.nutritious_food.helper.DateTimeHelper;
import com.spring.dev2chuc.nutritious_food.model.Combo;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class ComboDTO {

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
    private String createdAt;
    private String updatedAt;
    private int status;
    private Set<FoodDTO> foods = new HashSet<>();
    private Set<CategoryDTO> categories = new HashSet<>();

    public ComboDTO(Combo combo, boolean hasFood, boolean hasCategory) {
        this.id = combo.getId();
        this.name = combo.getName();
        this.description = combo.getDescription();
        this.image = combo.getImage();
        this.price = combo.getPrice();
        this.carbonhydrates = combo.getCarbonhydrates();
        this.protein = combo.getProtein();
        this.lipid = combo.getLipid();
        this.xenluloza = combo.getXenluloza();
        this.canxi = combo.getCanxi();
        this.iron = combo.getIron();
        this.zinc = combo.getZinc();
        this.vitaminA = combo.getVitaminA();
        this.vitaminB = combo.getVitaminB();
        this.vitaminC = combo.getVitaminC();
        this.vitaminD = combo.getVitaminD();
        this.vitaminE = combo.getVitaminE();
        this.calorie = combo.getCalorie();
        this.weight = combo.getWeight();
        this.createdAt = DateTimeHelper.formatDateFromLong(combo.getCreatedAt());
        this.updatedAt = DateTimeHelper.formatDateFromLong(combo.getUpdatedAt());
        this.status = combo.getStatus();
        if (hasFood)
            this.foods = combo.getFoods().stream().map(x -> new FoodDTO(x, false, false)).collect(Collectors.toSet());
        if (hasCategory)
            this.categories = combo.getCategories().stream().map(x -> new CategoryDTO(x, false, false)).collect(Collectors.toSet());
    }
}
