package com.spring.dev2chuc.nutritious_food.service.schedulecombo;

import com.spring.dev2chuc.nutritious_food.model.Combo;
import com.spring.dev2chuc.nutritious_food.model.Schedule;
import com.spring.dev2chuc.nutritious_food.model.ScheduleCombo;
import com.spring.dev2chuc.nutritious_food.model.Status;
import com.spring.dev2chuc.nutritious_food.payload.ScheduleComboRequest;
import com.spring.dev2chuc.nutritious_food.repository.ScheduleComboRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

@Service
public class ScheduleComboServiceImpl implements ScheduleComboService {

    @Autowired
    ScheduleComboRepository scheduleComboRepository;

    @Override
    public ScheduleCombo findById(Long id) {
        return scheduleComboRepository.findById(id).orElseThrow(null);
    }

    @Override
    public ScheduleCombo delete(ScheduleCombo scheduleCombo) {
        scheduleCombo.setStatus(Status.DEACTIVE.getValue());
        return scheduleComboRepository.save(scheduleCombo);
    }

    @Override
    public List<ScheduleCombo> findAllByCombo(Combo combo) {
        List<ScheduleCombo> list = scheduleComboRepository.findAllByCombo(combo);
        if (list == null) {
            throw new RuntimeException("Null pointer exception");
        }
        return list;
    }

    @Override
    public List<ScheduleCombo> findAllBySchedule(Schedule schedule) {
        List<ScheduleCombo> list = scheduleComboRepository.findAllBySchedule(schedule);
        if (list == null) {
            throw new RuntimeException("Null pointer exception");
        }
        return list;
    }

    @Override
    public ScheduleCombo findByStatusAndId(Integer status, Long id) {
        if (CollectionUtils.isEmpty(Collections.singleton(status))) {
            throw new RuntimeException("Null pointer exception");
        }

        if (CollectionUtils.isEmpty(Collections.singleton(id))) {
            throw new RuntimeException("Null pointer exception");
        }

        ScheduleCombo scheduleCombo = scheduleComboRepository.findByStatusAndId(status, id);
        if (scheduleCombo == null) {
            throw new RuntimeException("Null pointer exception");
        }
        return scheduleCombo;
    }

    @Override
    public ScheduleCombo store(ScheduleCombo scheduleCombo, ScheduleComboRequest scheduleComboRequest) {
        scheduleCombo.setDay(scheduleComboRequest.getDay());
        scheduleCombo.setType(scheduleComboRequest.getType());
        scheduleCombo.setStatus(Status.ACTIVE.getValue());
        return scheduleComboRepository.save(scheduleCombo);
    }
}
