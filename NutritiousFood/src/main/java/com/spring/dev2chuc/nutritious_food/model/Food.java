package com.spring.dev2chuc.nutritious_food.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.spring.dev2chuc.nutritious_food.model.audit.DateAudit;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "foods")
public class Food extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Column(columnDefinition = "TEXT")
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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "category_food", joinColumns = @JoinColumn(name = "food_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "combo_food", joinColumns = @JoinColumn(name = "food_id"), inverseJoinColumns = @JoinColumn(name = "combo_id"))
    private Set<Combo> combos = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "food")
    private Set<RattingFood> rattingFoods;

    public Food() {
    }

    public Food(String name, String description, String image, float price, float carbonhydrates, float protein, float lipid, float xenluloza, float canxi, float iron, float zinc, float vitaminA, float vitaminB, float vitaminC, float vitaminD, float vitaminE, float calorie, float weight) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.price = price;
        this.carbonhydrates = carbonhydrates;
        this.protein = protein;
        this.lipid = lipid;
        this.xenluloza = xenluloza;
        this.canxi = canxi;
        this.iron = iron;
        this.zinc = zinc;
        this.vitaminA = vitaminA;
        this.vitaminB = vitaminB;
        this.vitaminC = vitaminC;
        this.vitaminD = vitaminD;
        this.vitaminE = vitaminE;
        this.calorie = calorie;
        this.weight = weight;
        this.status = Status.ACTIVE.getValue();
    }
}
