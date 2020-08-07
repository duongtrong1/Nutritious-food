package com.spring.dev2chuc.nutritious_food.model;

import com.spring.dev2chuc.nutritious_food.model.audit.DateAudit;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "categories")
public class Category extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long parentId;
    private String name;
    private String image;

    @Column(columnDefinition = "TEXT")
    private String description;
    private Integer status;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "category_food", joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "food_id"))
    private Set<Food> foods = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "combo_category", joinColumns = @JoinColumn(name = "category_id"), inverseJoinColumns = @JoinColumn(name = "combo_id"))
    private Set<Combo> combos = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "schedule_category", joinColumns = @JoinColumn(name = "category_id"), inverseJoinColumns = @JoinColumn(name = "schedule_id"))
    private Set<Schedule> schedules = new HashSet<>();

    public Category(Long parentId, String name, String image, String description) {
        this.parentId = parentId;
        this.name = name;
        this.image = image;
        this.description = description;
        this.status = Status.ACTIVE.getValue();
    }

    public Category() {
    }
}
