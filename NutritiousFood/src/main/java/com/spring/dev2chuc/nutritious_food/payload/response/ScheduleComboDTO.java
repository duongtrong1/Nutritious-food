package com.spring.dev2chuc.nutritious_food.payload.response;

import com.spring.dev2chuc.nutritious_food.helper.DateTimeHelper;
import com.spring.dev2chuc.nutritious_food.model.Schedule;
import com.spring.dev2chuc.nutritious_food.model.ScheduleCombo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduleComboDTO {
    private Long id;
    private Integer day;
    private Integer type;
    private Integer status;
    private String createdAt;
    private String updatedAt;
    private ScheduleDTO schedule;
    private ComboDTO combo;

    public ScheduleComboDTO(ScheduleCombo scheduleCombo, boolean hasSchedule, boolean hasCombo) {
        this.id = scheduleCombo.getId();
        this.day = scheduleCombo.getDay();
        this.type = scheduleCombo.getType();
        this.status = scheduleCombo.getStatus();
        this.createdAt = DateTimeHelper.formatDateFromLong(scheduleCombo.getCreatedAt());
        this.updatedAt = DateTimeHelper.formatDateFromLong(scheduleCombo.getUpdatedAt());
        if (hasSchedule)
            this.schedule = new ScheduleDTO(scheduleCombo.getSchedule(), false, true);
        if (hasCombo)
            this.combo = new ComboDTO(scheduleCombo.getCombo(), false, false);
    }
}
