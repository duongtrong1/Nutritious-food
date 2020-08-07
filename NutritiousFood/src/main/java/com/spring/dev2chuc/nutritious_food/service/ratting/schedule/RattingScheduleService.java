package com.spring.dev2chuc.nutritious_food.service.ratting.schedule;

import com.spring.dev2chuc.nutritious_food.model.RattingSchedule;
import com.spring.dev2chuc.nutritious_food.payload.RattingScheduleRequest;

import java.util.List;

public interface RattingScheduleService {

    List<RattingSchedule> list();

    RattingSchedule merge(RattingSchedule rattingSchedule, RattingScheduleRequest rattingScheduleRequest);

    RattingSchedule getDetail(Long id);

    RattingSchedule update(RattingSchedule rattingSchedule, RattingScheduleRequest rattingScheduleRequest);
}
