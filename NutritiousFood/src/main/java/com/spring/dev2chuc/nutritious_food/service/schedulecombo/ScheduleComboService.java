package com.spring.dev2chuc.nutritious_food.service.schedulecombo;

import com.spring.dev2chuc.nutritious_food.model.Combo;
import com.spring.dev2chuc.nutritious_food.model.Schedule;
import com.spring.dev2chuc.nutritious_food.model.ScheduleCombo;
import com.spring.dev2chuc.nutritious_food.payload.ScheduleComboRequest;

import java.util.List;

public interface ScheduleComboService {

    ScheduleCombo findById(Long id);

    ScheduleCombo delete(ScheduleCombo scheduleCombo);

    List<ScheduleCombo> findAllByCombo(Combo combo);

    List<ScheduleCombo> findAllBySchedule(Schedule schedule);

    ScheduleCombo findByStatusAndId(Integer status, Long id);

    ScheduleCombo store(ScheduleCombo scheduleCombo, ScheduleComboRequest scheduleComboRequest);

}
