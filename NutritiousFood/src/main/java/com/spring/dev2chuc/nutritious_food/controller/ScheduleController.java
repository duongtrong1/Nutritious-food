package com.spring.dev2chuc.nutritious_food.controller;

import com.spring.dev2chuc.nutritious_food.model.Schedule;
import com.spring.dev2chuc.nutritious_food.model.Status;
import com.spring.dev2chuc.nutritious_food.payload.ScheduleRequest;
import com.spring.dev2chuc.nutritious_food.payload.response.*;
import com.spring.dev2chuc.nutritious_food.repository.ScheduleRepository;
import com.spring.dev2chuc.nutritious_food.service.schedule.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    ScheduleService scheduleService;

    @GetMapping("/")
    public ResponseEntity<?> getList() {
        List<Schedule> schedules = scheduleService.findAllByStatusIs(Status.ACTIVE.getValue());
        return new ResponseEntity<>(
                new ApiResponseCustom<>(
                        HttpStatus.OK.value(),
                        "OK",
                        schedules
                                .stream()
                                .map(x -> new ScheduleDTO(x, true, true))),
                HttpStatus.OK
        );
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody ScheduleRequest scheduleRequest) {
        Schedule result = scheduleService.store(scheduleRequest);
        return new ResponseEntity<>(new ApiResponseCustom<>(HttpStatus.CREATED.value(), "OK", new ScheduleDTO(result, false, true)), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDetail(@PathVariable("id") Long id) {
        Schedule schedule = scheduleService.findByStatusAndId(Status.ACTIVE.getValue(), id);
        if (schedule == null) {
            return new ResponseEntity<>(new ApiResponseError(HttpStatus.NOT_FOUND.value(), "Schedule not found"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new ApiResponseCustom<>(HttpStatus.OK.value(), "OK", new ScheduleDTO(schedule, true, true)), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody ScheduleRequest scheduleRequest, @PathVariable("id") Long id) {
        Schedule schedule = scheduleService.findByStatusAndId(Status.ACTIVE.getValue(), id);
        if (schedule == null) {
            return new ResponseEntity<>(new ApiResponseError(HttpStatus.NOT_FOUND.value(), "Schedule not found"), HttpStatus.NOT_FOUND);
        }
        Schedule result = scheduleService.update(schedule, scheduleRequest);
        return new ResponseEntity<>(new ApiResponseCustom<>(HttpStatus.CREATED.value(), "OK", new ScheduleDTO(result, true, true)), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        Schedule schedule = scheduleService.findByStatusAndId(Status.ACTIVE.getValue(), id);
        if (schedule == null) {
            return new ResponseEntity<>(new ApiResponseError(HttpStatus.NOT_FOUND.value(), "schedule not found"), HttpStatus.NOT_FOUND);
        }
        scheduleService.delete(schedule);
        return new ResponseEntity<>(new ApiResponseError(HttpStatus.OK.value(), "OK"), HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<?> getListPage(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "form", required = false) String form,
            @RequestParam(value = "to", required = false) String to,
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "6", required = false) int limit) {

        Specification specification = Specification.where(null);
        if (search != null && search.length() > 0) {
            specification = specification
                    .and(new SpecificationAll(new SearchCriteria("name", ":", search)))
                    .or(new SpecificationAll(new SearchCriteria("description", ":", search)));
        }
        specification = specification
                .and(new SpecificationAll(new SearchCriteria("createdAt", "orderBy", "desc")));

        specification = specification
                .and(new SpecificationAll(new SearchCriteria("status", ":", Status.ACTIVE.getValue())));

        Page<Schedule> schedules = scheduleService.schedulesWithPaginate(specification, page, limit);
        return new ResponseEntity<>(new ApiResponsePage<>(
                HttpStatus.OK.value(), "OK", schedules.stream().map(x -> new ScheduleDTO(x, false, true)).collect(Collectors.toList()),
                new RESTPagination(page, limit, schedules.getTotalPages(), schedules.getTotalElements())), HttpStatus.OK);
    }
}
