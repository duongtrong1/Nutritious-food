package com.spring.dev2chuc.nutritious_food.service.category;

import com.spring.dev2chuc.nutritious_food.model.Category;
import com.spring.dev2chuc.nutritious_food.payload.CategoryRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;
import java.util.List;

public interface CategoryService {

    List<Category> findByCategoriesByParentIdAndStatus(Long parentId, Integer status);

    List<Category> findAllByIdIn(Collection<Long> Ids);

    List<Category> findAll();

    List<Category> findAllByStatusIs(Integer status);

    Category findByIdAndStatus(Long id, Integer status);

    Category findById(Long id);

    Category merge(Category category, CategoryRequest categoryRequest);

    Category merge(Category category);

    Category update(Category category, CategoryRequest categoryRequest);

    Page<Category> categoriesWithPaginate(Specification specification, int page, int limit);
}
