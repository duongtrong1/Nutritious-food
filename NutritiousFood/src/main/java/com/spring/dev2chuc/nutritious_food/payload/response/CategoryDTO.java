package com.spring.dev2chuc.nutritious_food.payload.response;

import com.spring.dev2chuc.nutritious_food.helper.DateTimeHelper;
import com.spring.dev2chuc.nutritious_food.model.Category;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class CategoryDTO {
    private Long id;
    private Long parentId;
    private String name;
    private String image;
    private String description;
    private String createdAt;
    private String updatedAt;
    private Integer status;
    private Set<ComboDTO> combos = new HashSet<>();
    private Set<FoodDTO> foods = new HashSet<>();


    public CategoryDTO(Category category, boolean hasFood, boolean hasCombo) {
        this.id = category.getId();
        this.name = category.getName();
        this.description = category.getDescription();
        this.parentId = category.getParentId();
        this.image = category.getImage();
        this.status = category.getStatus();
        if (hasFood)
            this.foods = category.getFoods().stream().map(x -> new FoodDTO(x, false, false)).collect(Collectors.toSet());
        if (hasCombo)
            this.combos = category.getCombos().stream().map(x -> new ComboDTO(x, false, false)).collect(Collectors.toSet());
        this.createdAt = DateTimeHelper.formatDateFromLong(category.getCreatedAt());
        this.updatedAt = DateTimeHelper.formatDateFromLong(category.getUpdatedAt());
    }
}
