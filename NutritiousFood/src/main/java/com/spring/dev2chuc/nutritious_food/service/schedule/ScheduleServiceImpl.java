package com.spring.dev2chuc.nutritious_food.service.schedule;

import com.spring.dev2chuc.nutritious_food.model.Category;
import com.spring.dev2chuc.nutritious_food.model.Schedule;
import com.spring.dev2chuc.nutritious_food.model.Status;
import com.spring.dev2chuc.nutritious_food.model.UserProfile;
import com.spring.dev2chuc.nutritious_food.payload.ScheduleRequest;
import com.spring.dev2chuc.nutritious_food.repository.ScheduleRepository;
import com.spring.dev2chuc.nutritious_food.service.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    CategoryService categoryService;

    @Override
    public Schedule findById(Long id) {
        return scheduleRepository.findById(id).orElseThrow(null);
    }

    @Override
    public Schedule delete(Schedule schedule) {
        schedule.setStatus(Status.DEACTIVE.getValue());
        return scheduleRepository.save(schedule);
    }

    @Override
    public Schedule store(ScheduleRequest scheduleRequest) {
        Schedule schedule = new Schedule();
        schedule.setName(scheduleRequest.getName());
        schedule.setDescription(scheduleRequest.getDescription());
        schedule.setPrice(scheduleRequest.getPrice());
        schedule.setImage(scheduleRequest.getImage());
        schedule.setStatus(Status.ACTIVE.getValue());
        List<Long> categoryIds = scheduleRequest.getCategoryIds();
        if (categoryIds == null)
            categoryIds = new ArrayList<>();
        List<Category> categories = categoryService.findAllByIdIn(categoryIds);
        Set<Category> categorySet = new HashSet<>(categories);
        schedule.setCategories(categorySet);

        return scheduleRepository.save(schedule);
    }

    @Override
    public List<Schedule> findAllByStatusIs(Integer status) {
        return scheduleRepository.findAllByStatusIs(status);
    }

    @Override
    public Schedule update(Schedule schedule, ScheduleRequest scheduleRequest) {
        if (scheduleRequest.getName() != null) schedule.setName(scheduleRequest.getName());
        if (scheduleRequest.getDescription() != null) schedule.setDescription(scheduleRequest.getDescription());
        if (scheduleRequest.getPrice() != 0) schedule.setPrice(scheduleRequest.getPrice());
        if (scheduleRequest.getImage() != null) schedule.setImage(scheduleRequest.getImage());

        if (scheduleRequest.getCategoryIds().size() > 0) {
            List<Category> categories = categoryService.findAllByIdIn(scheduleRequest.getCategoryIds());
            Set<Category> categorySet = new HashSet<>(categories);
            schedule.setCategories(categorySet);
        }
        return scheduleRepository.save(schedule);
    }

    @Override
    public Schedule findByStatusAndId(Integer status, Long id) {
        return scheduleRepository.findByStatusAndId(status, id);
    }

    @Override
    public List<Schedule> suggest(UserProfile userProfile) {
        System.out.println(userProfile.getId());
        Set<Category> categories = userProfile.getCategories();
        System.out.println(userProfile.getCategories().size());
        List<Category> categoryList = new ArrayList<>(categories);
        System.out.println(categoryList.size());
        if (categoryList.isEmpty()) {
            categoryList = categoryService.findAll();
        }
        List<Schedule> scheduleList = scheduleRepository
                .findAllByStatusAndCategoriesIn(Status.ACTIVE.getValue(), categoryList)
                .stream()
                .limit(8)
                .collect(Collectors.toList());
        Set<Schedule> scheduleSet = new HashSet<>(scheduleList);
        List<Schedule> schedules = new ArrayList<>(scheduleSet);
        if (scheduleSet.size() <= 8) {
            int numberAdd = 8 - scheduleSet.size();
            List<Long> scheduleIds = scheduleSet.stream().map(Schedule::getId).collect(Collectors.toList());
            List<Schedule> scheduleOtherSet = scheduleRepository.findAllByIdNotInAndStatusIs(
                    scheduleIds,
                    Status.ACTIVE.getValue()
            ).stream().limit(numberAdd).collect(Collectors.toList());
            schedules.addAll(scheduleOtherSet);

        }
        return schedules;
    }

    @Override
    public Page<Schedule> schedulesWithPaginate(Specification specification, int page, int limit) {
        return scheduleRepository.findAll(specification, PageRequest.of(page - 1, limit));
    }
}
