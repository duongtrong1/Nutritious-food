package com.spring.dev2chuc.nutritious_food.service.ratting.combo;

import com.spring.dev2chuc.nutritious_food.model.RattingCombo;
import com.spring.dev2chuc.nutritious_food.payload.RattingComboRequest;

import java.util.List;

public interface RattingComboService {
    List<RattingCombo> list();

    RattingCombo merge(RattingCombo rattingCombo, RattingComboRequest rattingComboRequest);

    RattingCombo getDetail(Long id);

    RattingCombo update(RattingCombo rattingCombo, RattingComboRequest rattingComboRequest);
}
