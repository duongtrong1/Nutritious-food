package com.spring.dev2chuc.nutritious_food.service.history;

import com.spring.dev2chuc.nutritious_food.model.Food;
import com.spring.dev2chuc.nutritious_food.model.History;
import com.spring.dev2chuc.nutritious_food.model.User;
import com.spring.dev2chuc.nutritious_food.payload.HistoryRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface HistoryService {

    Page<History> getAllWithPaginate(Specification specification, int page, int limit);

    History getDetailById(Long id);

    History store(HistoryRequest historyRequest, User user, Food food);

    List<History> getAllByCreatedAtBetween(String from, String to);

    List<History> getAllByUser(User user);

    History save(History history);
}
