package com.spring.dev2chuc.nutritious_food.model;

import com.spring.dev2chuc.nutritious_food.model.audit.DateAudit;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "devices")
public class Device extends DateAudit {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Integer status;

    public Device(String id, User user) {
        this.id = id;
        this.user = user;
        this.status = Status.ACTIVE.getValue();
    }

    public Device() {
    }
}
