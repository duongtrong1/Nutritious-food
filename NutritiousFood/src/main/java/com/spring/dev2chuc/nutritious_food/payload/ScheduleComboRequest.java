package com.spring.dev2chuc.nutritious_food.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduleComboRequest {
    private Long comboId;
    private Long scheduleId;
    private Integer day;
    private Integer type;
}
