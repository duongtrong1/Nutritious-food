package com.spring.dev2chuc.nutritious_food.model;

import com.spring.dev2chuc.nutritious_food.model.audit.DateAudit;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "histories")
public class History extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private float calorie;
    @Column(columnDefinition = "TEXT")
    private String comment;
    private int type;
    private int status;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "food_id", nullable = false)
    private Food food;

    public History(float calorie, String comment, int type, User user, Food food) {
        this.calorie = calorie;
        this.comment = comment;
        this.type = type;
        this.status = Status.ACTIVE.getValue();
        this.user = user;
        this.food = food;
    }

    public History() {
    }
}
