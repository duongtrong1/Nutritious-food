package com.spring.dev2chuc.nutritious_food.controller;

import com.spring.dev2chuc.nutritious_food.model.Food;
import com.spring.dev2chuc.nutritious_food.model.History;
import com.spring.dev2chuc.nutritious_food.model.User;
import com.spring.dev2chuc.nutritious_food.payload.HistoryRequest;
import com.spring.dev2chuc.nutritious_food.payload.response.*;
import com.spring.dev2chuc.nutritious_food.repository.FoodRepository;
import com.spring.dev2chuc.nutritious_food.service.food.FoodService;
import com.spring.dev2chuc.nutritious_food.service.history.HistoryService;
import com.spring.dev2chuc.nutritious_food.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/history")
public class HistoryController {
    @Autowired
    FoodService foodService;

    @Autowired
    HistoryService historyService;

    @Autowired
    UserService userService;

    @Autowired
    FoodRepository foodRepository;

    @GetMapping
    public ResponseEntity<?> getList(@RequestParam(value = "search", required = false) String search,
                                     @RequestParam(value = "from", required = false) String from,
                                     @RequestParam(value = "to", required = false) String to,
                                     @RequestParam(defaultValue = "1", required = false) int page,
                                     @RequestParam(defaultValue = "12", required = false) int limit) {
        User user = userService.getUserAuth();
        if (user == null) {
            return new ResponseEntity<>(new ApiResponseError(HttpStatus.NOT_FOUND.value(), "User not found"), HttpStatus.NOT_FOUND);
        } else {
            Specification specification = Specification.where(null);
            List<History> histories = historyService.getAllByUser(user);
            Long[] historyIds;
            if (histories.size() == 0) {
                historyIds = new Long[]{Long.valueOf(0)};
            } else {
                historyIds = histories.stream().map(History::getId).toArray(Long[]::new);
            }
            specification = specification
                    .and(new SpecificationAll(new SearchCriteria("id", "in", historyIds)));


            if (search != null && search.length() > 0) {
                List<Food> foods = foodService.searchByNameAndDescription(search);
                Long[] foodIds;
                if (foods.size() == 0) {
                    foodIds = new Long[]{Long.valueOf(0)};
                } else {
                    foodIds = foods.stream().map(Food::getId).toArray(Long[]::new);
                }
                specification = specification
                        .and(new SpecificationAll(new SearchCriteria("food_id", "in", foodIds)));
            }
            specification = specification
                    .and(new SpecificationAll(new SearchCriteria("createdAt", "orderBy", "desc")));

            if (from != null && to != null) {
                System.out.println(from);
                System.out.println(to);
                List<History> historyList = historyService.getAllByCreatedAtBetween(from, to);
                if (historyList.size() == 0) {
                    historyIds = new Long[]{Long.valueOf(0)};
                } else {
                    historyIds = historyList.stream().map(History::getId).toArray(Long[]::new);
                }
                System.out.println(historyIds);

                specification = specification
                        .and(new SpecificationAll(new SearchCriteria("id", "in", historyIds)));
            }

            Page<History> historyPage  = historyService.getAllWithPaginate(specification, page, limit);
            return new ResponseEntity<>(new ApiResponsePage<>(
                    HttpStatus.OK.value(), "Get your history success", historyPage.stream()
                    .map(x -> new HistoryDTO(x, true, true))
                    .collect(Collectors.toList()),
                    new RESTPagination(page, limit, historyPage.getTotalPages(), historyPage.getTotalElements())), HttpStatus.OK);
        }
    }

    @PostMapping
    public ResponseEntity<?> store(@Valid @RequestBody HistoryRequest historyRequest) {
        User user = userService.getUserAuth();
        if (user == null) {
            return new ResponseEntity<>(new ApiResponseError(HttpStatus.NOT_FOUND.value(), "User not found"), HttpStatus.NOT_FOUND);
        } else {
            List<History> histories = new ArrayList<>();
            for (Long foodId : historyRequest.getFoodIds()) {
                Food food = foodService.findById(foodId);
                if (food == null) {
                    return new ResponseEntity<>(new ApiResponseCustom<>(HttpStatus.NOT_FOUND.value(),
                            "Food not found"),
                            HttpStatus.NOT_FOUND
                    );
                }
                History history = historyService.store(historyRequest, user, food);
                if (history != null) {
                    histories.add(history);
                } else {
                    return new ResponseEntity<>(new ApiResponseCustom<>(HttpStatus.NOT_FOUND.value(),
                            "History store error"),
                            HttpStatus.NOT_FOUND
                    );
                }
            }

            if (histories == null) {
                return new ResponseEntity<>(new ApiResponseCustom<>(HttpStatus.NOT_FOUND.value(),
                        "Store history success"),
                        HttpStatus.NOT_FOUND
                );
            }
            return new ResponseEntity<>(new ApiResponseCustom<>(HttpStatus.CREATED.value(),
                    "Store history success",
                    histories.stream().map(x -> new HistoryDTO(x, true, true)).collect(Collectors.toList())),
                    HttpStatus.CREATED
            );
        }

    }
}
