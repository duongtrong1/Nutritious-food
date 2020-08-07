package com.spring.dev2chuc.nutritious_food.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.spring.dev2chuc.nutritious_food.model.audit.DateAudit;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class ScheduleCombo extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "combo_id", nullable = false)
    private Combo combo;

    @ManyToOne
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    private Integer day;
    private Integer type;
    private Integer status;

    public ScheduleCombo(Combo combo, Schedule schedule, Integer day, Integer type) {
        this.combo = combo;
        this.schedule = schedule;
        this.day = day;
        this.type = type;
        this.status = Status.ACTIVE.getValue();
    }

    public ScheduleCombo(Integer day, Integer type) {
        this.day = day;
        this.type = type;
    }

    public ScheduleCombo() {
    }
}
