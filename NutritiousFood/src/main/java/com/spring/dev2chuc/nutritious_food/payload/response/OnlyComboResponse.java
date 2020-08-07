package com.spring.dev2chuc.nutritious_food.payload.response;

import com.spring.dev2chuc.nutritious_food.helper.DateTimeHelper;
import com.spring.dev2chuc.nutritious_food.model.Combo;
import com.spring.dev2chuc.nutritious_food.model.audit.DateAudit;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class OnlyComboResponse {

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

    public OnlyComboResponse(Combo combo) {
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
        this.status = combo.getStatus();
        this.createdAt = DateTimeHelper.formatDateFromLong(combo.getCreatedAt());
        this.updatedAt = DateTimeHelper.formatDateFromLong(combo.getUpdatedAt());
    }
}
