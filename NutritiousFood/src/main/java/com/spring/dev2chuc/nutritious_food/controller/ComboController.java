package com.spring.dev2chuc.nutritious_food.controller;

import com.spring.dev2chuc.nutritious_food.model.*;
import com.spring.dev2chuc.nutritious_food.payload.ComboRequest;
import com.spring.dev2chuc.nutritious_food.payload.response.*;
import com.spring.dev2chuc.nutritious_food.service.combo.ComboService;
import com.spring.dev2chuc.nutritious_food.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("/api/combo")
public class ComboController {

    @Autowired
    ComboService comboService;

    @Autowired
    UserService userService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody ComboRequest comboRequest) {
        User user = userService.getUserAuth();
        if (user == null) {
            return new ResponseEntity<>(new ApiResponseError(HttpStatus.NOT_FOUND.value(), "User not found"), HttpStatus.NOT_FOUND);
        } else {
            Combo combo = new Combo();
            combo.setUser(user);
            Combo result = comboService.merge(combo, comboRequest);
            return new ResponseEntity<>(new ApiResponseCustom<>(HttpStatus.CREATED.value(), "Create success", new ComboDTO(result, true, true)), HttpStatus.CREATED);
        }
    }

    @GetMapping
    public ResponseEntity<?> getList() {
        List<Combo> combos = comboService.findAll();
        return new ResponseEntity<>(new ApiResponseCustom<>(HttpStatus.OK.value(), "OK", combos.stream().map(x -> new ComboDTO(x, true, true)).collect(Collectors.toList())), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody ComboRequest comboRequest, @PathVariable Long id) {
        Combo combo = comboService.findById(id);
        if (combo == null) {
            return new ResponseEntity<>(new ApiResponseError(HttpStatus.NOT_FOUND.value(), "Combo not found"), HttpStatus.NOT_FOUND);
        }

        Combo result = comboService.update(combo, comboRequest);
        return new ResponseEntity<>(new ApiResponseCustom<>(HttpStatus.OK.value(), "Update success", new ComboDTO(result, true, true)), HttpStatus.OK);
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<?> show(@PathVariable("id") Long id) {
        Combo combo = comboService.findByStatusAndId(Status.ACTIVE.getValue(), id);
        if (combo == null) {
            return new ResponseEntity<>(new ApiResponseError(HttpStatus.OK.value(), "Combo not found"), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new ApiResponseCustom<>(HttpStatus.OK.value(), "OK", new ComboDTO(combo, true, true)), HttpStatus.OK);
    }


    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        Combo combo = comboService.findByStatusAndId(Status.ACTIVE.getValue(), id);
        if (combo == null) {
            return new ResponseEntity<>(new ApiResponseCustom(HttpStatus.OK.value(), "Combo not found"), HttpStatus.NOT_FOUND);
        }
        combo.setStatus(Status.DEACTIVE.getValue());
        comboService.merge(combo);
        return new ResponseEntity<>(new ApiResponseError(HttpStatus.OK.value(), "OK"), HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<?> getListPage(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "from", required = false) String from,
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

        Page<Combo> combos = comboService.combosWithPaginate(specification, page, limit);
        return new ResponseEntity<>(new ApiResponsePage<>(
                HttpStatus.OK.value(), "OK", combos.stream().map(x -> new OnlyComboResponse(x)).collect(Collectors.toList()),
                new RESTPagination(page, limit, combos.getTotalPages(), combos.getTotalElements())), HttpStatus.OK);
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<?> getListByCategory(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "form", required = false) String form,
            @RequestParam(value = "to", required = false) String to,
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "6", required = false) int limit,
            @PathVariable("id") Long id) {
        List<Combo> comboList = comboService.findAllByCategory(id);
        System.out.println(id);
        Long[] comboIds;
        if (comboList == null || comboList.size() == 0) {
            comboIds = new Long[]{Long.valueOf(0)};
        } else {
            comboIds = comboList.stream().map(Combo::getId).toArray(Long[]::new);
        }
        Specification specification = Specification.where(null);
        if (search != null && search.length() > 0) {
            specification = specification
                    .and(new SpecificationAll(new SearchCriteria("name", ":", search)))
                    .or(new SpecificationAll(new SearchCriteria("description", ":", search)));
        }

        if (comboIds.length == 0) {
            return new ResponseEntity<>(new ApiResponsePage<>(
                    HttpStatus.OK.value(), "OK", new Long[]{},
                    new RESTPagination(page, limit, 0, 0)), HttpStatus.OK);
        }

        specification = specification
                .and(new SpecificationAll(new SearchCriteria("id", "in", comboIds)));

        specification = specification
                .and(new SpecificationAll(new SearchCriteria("createdAt", "orderBy", "desc")));

        specification = specification
                .and(new SpecificationAll(new SearchCriteria("status", ":", Status.ACTIVE.getValue())));

        Page<Combo> combos = comboService.combosWithPaginate(specification, page, limit);
        return new ResponseEntity<>(new ApiResponsePage<>(
                HttpStatus.OK.value(), "OK", combos.stream()
                .map(x -> new OnlyComboResponse(x))
                .collect(Collectors.toList()),
                new RESTPagination(page, limit, combos.getTotalPages(), combos.getTotalElements())), HttpStatus.OK);
    }
}
