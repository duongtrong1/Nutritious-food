package com.spring.dev2chuc.nutritious_food.service.history;

import com.spring.dev2chuc.nutritious_food.model.Food;
import com.spring.dev2chuc.nutritious_food.model.History;
import com.spring.dev2chuc.nutritious_food.model.User;
import com.spring.dev2chuc.nutritious_food.model.audit.DateAudit;
import com.spring.dev2chuc.nutritious_food.payload.HistoryRequest;
import com.spring.dev2chuc.nutritious_food.repository.FoodRepository;
import com.spring.dev2chuc.nutritious_food.repository.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class HistoryServiceImpl implements HistoryService {

    @Autowired
    FoodRepository foodRepository;

    @Autowired
    HistoryRepository historyRepository;

    @Override
    public Page getAllWithPaginate(Specification specification, int page, int limit) {
        return historyRepository.findAll(specification, PageRequest.of(page - 1, limit));
    }

    @Override
    public History getDetailById(Long id) {
        return historyRepository.findById(id).orElseThrow(null);
    }

    @Override
    public History store(HistoryRequest historyRequest, User user, Food food) {
        History history = new History(food.getCalorie(), historyRequest.getComment(), historyRequest.getType(), user, food);
        return historyRepository.save(history);
    }

    @Override
    public List<History> getAllByCreatedAtBetween(String from, String to) {
        Instant fromInstant = DateAudit.stringToInstant(from);
        Instant toInstant = DateAudit.stringToInstant(to);
        return historyRepository.findAllByCreatedAtBetween(fromInstant, toInstant);
    }

    @Override
    public List<History> getAllByUser(User user) {
        return historyRepository.findAllByUser(user);
    }

    @Override
    public History save(History history) {
        return historyRepository.save(history);
    }


}
