package com.spring.dev2chuc.nutritious_food.service.ratting.combo;

import com.spring.dev2chuc.nutritious_food.model.Combo;
import com.spring.dev2chuc.nutritious_food.model.RattingCombo;
import com.spring.dev2chuc.nutritious_food.model.User;
import com.spring.dev2chuc.nutritious_food.payload.RattingComboRequest;
import com.spring.dev2chuc.nutritious_food.repository.RattingComboRepository;
import com.spring.dev2chuc.nutritious_food.service.combo.ComboService;
import com.spring.dev2chuc.nutritious_food.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

@Service
public class RattingComboServiceImpl implements RattingComboService {

    @Autowired
    RattingComboRepository rattingComboRepository;

    @Autowired
    ComboService comboService;

    @Autowired
    UserService userService;

    @Override
    public List<RattingCombo> list() {
        return rattingComboRepository.findAll();
    }

    @Override
    public RattingCombo merge(RattingCombo rattingCombo, RattingComboRequest rattingComboRequest) {
        rattingCombo.setRate(rattingComboRequest.getRate());
        rattingCombo.setComment(rattingComboRequest.getComment());
        rattingCombo.setImage(rattingComboRequest.getImage());

        User user = userService.getById(rattingComboRequest.getUserId());
        if (user == null) {
            throw new RuntimeException("Null pointer exception");
        }
        Combo combo = comboService.findById(rattingComboRequest.getComboId());
        if (combo == null) {
            throw new RuntimeException("Null pointer exception");
        }

        rattingCombo.setUser(user);
        rattingCombo.setCombo(combo);
        RattingCombo result = rattingComboRepository.save(rattingCombo);
        return result;
    }

    @Override
    public RattingCombo getDetail(Long id) {
        if (CollectionUtils.isEmpty(Collections.singleton(id))) {
            throw new RuntimeException("Null pointer exception...");
        }
        return rattingComboRepository.findById(id).orElseThrow(null);
    }

    @Override
    public RattingCombo update(RattingCombo rattingCombo, RattingComboRequest rattingComboRequest) {
        if (rattingComboRequest.getRate() != null) rattingCombo.setRate(rattingComboRequest.getRate());
        if (rattingComboRequest.getComment() != null) rattingCombo.setComment(rattingComboRequest.getComment());
        if (rattingComboRequest.getImage() != null) rattingCombo.setImage(rattingComboRequest.getImage());

        User user = userService.getById(rattingComboRequest.getUserId());
        if (user == null) {
            throw new RuntimeException("Null pointer exception");
        }
        rattingCombo.setUser(user);

        Combo combo = comboService.findById(rattingComboRequest.getComboId());
        if (combo == null) {
            throw new RuntimeException("Null pointer exception");
        }
        rattingCombo.setCombo(combo);

        RattingCombo result = rattingComboRepository.save(rattingCombo);
        return result;
    }
}
