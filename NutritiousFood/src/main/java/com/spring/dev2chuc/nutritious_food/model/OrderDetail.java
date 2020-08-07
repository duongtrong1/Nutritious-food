package com.spring.dev2chuc.nutritious_food.model;

import com.spring.dev2chuc.nutritious_food.model.audit.DateAudit;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "order_detail")
public class OrderDetail extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "food_id", nullable = true)
    private Food food;

    @ManyToOne
    @JoinColumn(name = "combo_id", nullable = true)
    private Combo combo;

    @ManyToOne
    @JoinColumn(name = "schedule_id", nullable = true)
    private Schedule schedule;

    private Integer quantity;
    private Integer type;
    private float price;
    private Integer status;

    public OrderDetail(Order order, Food food, Combo combo, Schedule schedule, Integer quantity, float price) {
        this.order = order;
        this.food = food;
        this.combo = combo;
        this.schedule = schedule;
        this.quantity = quantity;
        this.price = price;
        this.status = Status.ACTIVE.getValue();
    }

    public OrderDetail(Order order, Food food, Integer quantity, float price) {
        this.order = order;
        this.food = food;
        this.quantity = quantity;
        this.type = 1;
        this.price = price;
        this.status = Status.ACTIVE.getValue();
    }

    public OrderDetail(Order order, Combo combo, Integer quantity, float price) {
        this.order = order;
        this.combo = combo;
        this.quantity = quantity;
        this.type = 2;
        this.price = price;
        this.status = Status.ACTIVE.getValue();
    }

    public OrderDetail(Order order, Schedule schedule, Integer quantity, float price) {
        this.order = order;
        this.schedule = schedule;
        this.quantity = quantity;
        this.type = 3;
        this.price = price;
        this.status = Status.ACTIVE.getValue();
    }

    public OrderDetail() {
    }
}
