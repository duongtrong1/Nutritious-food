package com.spring.dev2chuc.nutritious_food.controller;

import com.spring.dev2chuc.nutritious_food.model.RattingCombo;
import com.spring.dev2chuc.nutritious_food.payload.RattingComboRequest;
import com.spring.dev2chuc.nutritious_food.payload.response.ApiResponseCustom;
import com.spring.dev2chuc.nutritious_food.payload.response.ApiResponseError;
import com.spring.dev2chuc.nutritious_food.service.combo.ComboService;
import com.spring.dev2chuc.nutritious_food.service.ratting.combo.RattingComboService;
import com.spring.dev2chuc.nutritious_food.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/ratting-combo")
public class RattingComboController {

    @Autowired
    RattingComboService rattingComboService;

    @Autowired
    UserService userService;

    @Autowired
    ComboService comboService;

    @GetMapping
    public ResponseEntity<?> getList() {
        List<RattingCombo> list = rattingComboService.list();
        return new ResponseEntity<>(new ApiResponseCustom<>(HttpStatus.OK.value(), "OK", list), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody RattingComboRequest rattingComboRequest) {
        RattingCombo rattingCombo = new RattingCombo();
        RattingCombo result = rattingComboService.merge(rattingCombo, rattingComboRequest);
        return new ResponseEntity<>(new ApiResponseCustom<>(HttpStatus.CREATED.value(), "Create success", result), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody RattingComboRequest rattingComboRequest, @PathVariable Long id) {
        RattingCombo rattingCombo = rattingComboService.getDetail(id);
        if (rattingCombo == null) {
            return new ResponseEntity<>(new ApiResponseError(HttpStatus.NOT_FOUND.value(), "Ratting combo not found"), HttpStatus.NOT_FOUND);
        }
        RattingCombo result = rattingComboService.update(rattingCombo, rattingComboRequest);
        return new ResponseEntity<>(new ApiResponseCustom<>(HttpStatus.OK.value(), "OK", result), HttpStatus.OK);
    }
}
