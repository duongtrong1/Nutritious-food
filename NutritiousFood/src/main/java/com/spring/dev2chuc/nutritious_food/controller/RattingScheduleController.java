package com.spring.dev2chuc.nutritious_food.controller;

import com.spring.dev2chuc.nutritious_food.model.RattingSchedule;
import com.spring.dev2chuc.nutritious_food.payload.RattingScheduleRequest;
import com.spring.dev2chuc.nutritious_food.payload.response.ApiResponseCustom;
import com.spring.dev2chuc.nutritious_food.payload.response.ApiResponseError;
import com.spring.dev2chuc.nutritious_food.repository.RattingScheduleRepository;
import com.spring.dev2chuc.nutritious_food.repository.ScheduleRepository;
import com.spring.dev2chuc.nutritious_food.repository.UserRepository;
import com.spring.dev2chuc.nutritious_food.service.ratting.schedule.RattingScheduleService;
import com.spring.dev2chuc.nutritious_food.service.schedule.ScheduleService;
import com.spring.dev2chuc.nutritious_food.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/ratting-schedule")
public class RattingScheduleController {

    @Autowired
    RattingScheduleRepository rattingScheduleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    RattingScheduleService rattingScheduleService;

    @Autowired
    UserService userService;

    @Autowired
    ScheduleService scheduleService;

    @GetMapping
    public ResponseEntity<?> getList() {
        List<RattingSchedule> list = rattingScheduleService.list();
        return new ResponseEntity<>(new ApiResponseCustom<>(HttpStatus.OK.value(), "OK", list), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody RattingScheduleRequest rattingScheduleRequest) {
        RattingSchedule rattingSchedule = new RattingSchedule();
        RattingSchedule result = rattingScheduleService.merge(rattingSchedule, rattingScheduleRequest);
        return new ResponseEntity<>(new ApiResponseCustom<>(HttpStatus.CREATED.value(), "Create success", result), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody RattingScheduleRequest rattingScheduleRequest, @PathVariable Long id) {
        RattingSchedule rattingSchedule = rattingScheduleService.getDetail(id);
        if (rattingSchedule == null) {
            return new ResponseEntity<>(new ApiResponseError(HttpStatus.NOT_FOUND.value(), "Ratting schedule not found"), HttpStatus.NOT_FOUND);
        }
        RattingSchedule result = rattingScheduleService.update(rattingSchedule, rattingScheduleRequest);
        return new ResponseEntity<>(new ApiResponseCustom<>(HttpStatus.OK.value(), "OK", result), HttpStatus.OK);
    }
}
