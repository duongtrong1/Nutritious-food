package com.spring.dev2chuc.nutritious_food.controller;

import com.spring.dev2chuc.nutritious_food.helper.CategoryHelper;
import com.spring.dev2chuc.nutritious_food.model.Category;
import com.spring.dev2chuc.nutritious_food.model.Status;
import com.spring.dev2chuc.nutritious_food.payload.CategoryRequest;
import com.spring.dev2chuc.nutritious_food.payload.response.*;
import com.spring.dev2chuc.nutritious_food.service.category.CategoryService;
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

@CrossOrigin
@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @PostMapping("/create")
    public ResponseEntity<?> store(@Valid @RequestBody CategoryRequest categoryRequest) {
        Category current = new Category();
        Category result = categoryService.merge(current, categoryRequest);
        return new ResponseEntity<>(new ApiResponseCustom<>(HttpStatus.CREATED.value(), "Create success", new CategoryDTO(result, true, true)), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable("id") Long id) {
        Category result = categoryService.findByIdAndStatus(id, Status.ACTIVE.getValue());
        return new ResponseEntity<>(new ApiResponseCustom<>(HttpStatus.OK.value(),
                "OK",
                new CategoryDTO(result, true, true)
        ), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody CategoryRequest categoryRequest, @PathVariable("id") Long id) {
        Category category = categoryService.findByIdAndStatus(id, Status.ACTIVE.getValue());
        if (category == null) {
            return new ResponseEntity<>(new ApiResponseError(HttpStatus.NOT_FOUND.value(), "Category not found"), HttpStatus.NOT_FOUND);
        }
        Category result = categoryService.merge(category, categoryRequest);
        return new ResponseEntity<>(new ApiResponseCustom<>(HttpStatus.OK.value(), "Update success", new CategoryDTO(result, true, true)), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        Category result = categoryService.findByIdAndStatus(id, Status.ACTIVE.getValue());
        if (result == null) {
            return new ResponseEntity<>(new ApiResponseCustom(HttpStatus.NOT_FOUND.value(), "Category not found"), HttpStatus.NOT_FOUND);
        }
        result.setStatus(Status.DEACTIVE.getValue());
        categoryService.merge(result);
        return new ResponseEntity<>(new ApiResponseError(HttpStatus.OK.value(), "OK"), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> listAll() {
        List<Category> result = categoryService.findAllByStatusIs(Status.ACTIVE.getValue());
        List<CategoryDTO> categoryDTO = result.stream().map(x -> new CategoryDTO(x, true, true)).collect(Collectors.toList());
        return new ResponseEntity<>(new ApiResponseCustom<>(HttpStatus.OK.value(), "OK", categoryDTO), HttpStatus.OK);
    }

    @GetMapping("/parent/{id}")
    public ResponseEntity<?> getByParentId(@PathVariable("id") Long id) {
        List<Category> result = categoryService.findByCategoriesByParentIdAndStatus(id, Status.ACTIVE.getValue());
        List<CategoryDTO> categoryDTO = result.stream().map(x -> new CategoryDTO(x, true, true)).collect(Collectors.toList());
        return new ResponseEntity<>(new ApiResponseCustom<>(HttpStatus.OK.value(), "OK", categoryDTO), HttpStatus.OK);
    }

    @GetMapping("/latest")
    public ResponseEntity<?> latest() {
        List<Category> categoryList = categoryService.findAll();
        List<Category> categoryResult = new ArrayList<Category>();
        Long parentId = Long.valueOf("0");
        List<Category> result = CategoryHelper.recusiveCategory(categoryList, parentId, "", categoryResult);
        return new ResponseEntity<>(new ApiResponseCustom<>(HttpStatus.OK.value(), "OK", result), HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<?> getListPage(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "from", required = false) String from,
            @RequestParam(value = "to", required = false) String to,
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int limit) {

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

        Page<Category> categories = categoryService.categoriesWithPaginate(specification, page, limit);
        List<CategoryDTO> categoryDTO = categories.stream().map(x -> new CategoryDTO(x, true, true)).collect(Collectors.toList());
        return new ResponseEntity<>(new ApiResponsePage<>(
                HttpStatus.OK.value(), "OK", categoryDTO,
                new RESTPagination(page, limit, categories.getTotalPages(), categories.getTotalElements())), HttpStatus.OK);
    }
}
