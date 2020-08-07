package com.spring.dev2chuc.nutritious_food.service.ratting.schedule;

import com.spring.dev2chuc.nutritious_food.model.RattingSchedule;
import com.spring.dev2chuc.nutritious_food.model.Schedule;
import com.spring.dev2chuc.nutritious_food.model.User;
import com.spring.dev2chuc.nutritious_food.payload.RattingScheduleRequest;
import com.spring.dev2chuc.nutritious_food.repository.RattingScheduleRepository;
import com.spring.dev2chuc.nutritious_food.service.schedule.ScheduleService;
import com.spring.dev2chuc.nutritious_food.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

@Service
public class RattingScheduleServiceImpl implements RattingScheduleService {

    @Autowired
    RattingScheduleRepository rattingScheduleRepository;

    @Autowired
    UserService userService;

    @Autowired
    ScheduleService scheduleService;

    @Override
    public List<RattingSchedule> list() {
        return rattingScheduleRepository.findAll();
    }

    @Override
    public RattingSchedule merge(RattingSchedule rattingSchedule, RattingScheduleRequest rattingScheduleRequest) {
        rattingSchedule.setRate(rattingScheduleRequest.getRate());
        rattingSchedule.setComment(rattingScheduleRequest.getComment());
        rattingSchedule.setImage(rattingScheduleRequest.getImage());

        User user = userService.getById(rattingScheduleRequest.getUserId());
        Schedule schedule = scheduleService.findById(rattingScheduleRequest.getScheduleId());

        rattingSchedule.setUser(user);
        rattingSchedule.setSchedule(schedule);
        return rattingScheduleRepository.save(rattingSchedule);
    }

    @Override
    public RattingSchedule getDetail(Long id) {
        if (CollectionUtils.isEmpty(Collections.singleton(id))) {
            throw new RuntimeException("Null pointer exception");
        }
        return rattingScheduleRepository.findById(id).orElseThrow(null);
    }

    @Override
    public RattingSchedule update(RattingSchedule rattingSchedule, RattingScheduleRequest rattingScheduleRequest) {
        if (rattingScheduleRequest.getRate() != null) rattingSchedule.setRate(rattingScheduleRequest.getRate());
        if (rattingScheduleRequest.getComment() != null)
            rattingSchedule.setComment(rattingScheduleRequest.getComment());
        if (rattingScheduleRequest.getImage() != null) rattingSchedule.setImage(rattingScheduleRequest.getImage());

        User user = userService.getById(rattingScheduleRequest.getUserId());
        if (user == null) {
            throw new RuntimeException("Null pointer exception");
        }

        Schedule schedule = scheduleService.findById(rattingScheduleRequest.getScheduleId());
        if (schedule == null) {
            throw new RuntimeException("Null pointer exception");
        }

        rattingSchedule.setUser(user);
        rattingSchedule.setSchedule(schedule);
        return rattingScheduleRepository.save(rattingSchedule);
    }
}
