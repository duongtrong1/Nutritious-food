package com.spring.dev2chuc.nutritious_food.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.spring.dev2chuc.nutritious_food.model.audit.DateAudit;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class RattingSchedule extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer rate;
    @Column(columnDefinition = "TEXT")
    private String comment;
    private String image;
    private Integer status;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    public RattingSchedule() {
    }

    public RattingSchedule(Integer rate, String comment, String image) {
        this.rate = rate;
        this.comment = comment;
        this.image = image;
    }

    public RattingSchedule(Integer rate, String comment, String image, User user, Schedule schedule) {
        this.rate = rate;
        this.comment = comment;
        this.image = image;
        this.user = user;
        this.schedule = schedule;
        this.status = Status.ACTIVE.getValue();
    }
}
