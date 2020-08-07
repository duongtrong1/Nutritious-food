package com.spring.dev2chuc.nutritious_food.service.category;

import com.spring.dev2chuc.nutritious_food.model.Category;
import com.spring.dev2chuc.nutritious_food.model.Status;
import com.spring.dev2chuc.nutritious_food.payload.CategoryRequest;
import com.spring.dev2chuc.nutritious_food.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public List<Category> findByCategoriesByParentIdAndStatus(Long parentId, Integer status) {
        List<Category> list = categoryRepository.queryCategoriesByParentIdAndStatus(parentId, status);
        return list;
    }

    @Override
    public List<Category> findAllByIdIn(Collection<Long> Ids) {
        return categoryRepository.findAllByIdIn(Ids);
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public List<Category> findAllByStatusIs(Integer status) {
        List<Category> list = categoryRepository.findAllByStatusIs(status);
        return list;
    }

    @Override
    public Category findByIdAndStatus(Long id, Integer status) {
        Category category = categoryRepository.findByIdAndStatus(id, status);
        return category;
    }

    @Override
    public Category findById(Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        return category.orElse(null);
    }

    @Override
    public Category merge(Category category, CategoryRequest categoryRequest) {
        if (categoryRequest.getParentId() != null) category.setParentId(categoryRequest.getParentId());
        else category.setParentId(Long.valueOf(0));
        if (categoryRequest.getName() != null) category.setName(categoryRequest.getName());
        if (categoryRequest.getDescription() != null) category.setDescription(categoryRequest.getDescription());
        if (categoryRequest.getImage() != null) category.setImage(categoryRequest.getImage());
        if (category.getStatus() == null) category.setStatus(Status.ACTIVE.getValue());
        return categoryRepository.save(category);
    }

    @Override
    public Category merge(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category update(Category category, CategoryRequest categoryRequest) {
        if (categoryRequest.getName() != null) category.setName(categoryRequest.getName());
        if (categoryRequest.getImage() != null) category.setImage(categoryRequest.getImage());
        if (categoryRequest.getDescription() != null) category.setDescription(categoryRequest.getDescription());
        if (categoryRequest.getParentId() != null) category.setParentId(categoryRequest.getParentId());
        return categoryRepository.save(category);
    }

    @Override
    public Page<Category> categoriesWithPaginate(Specification specification, int page, int limit) {
        List<Category> categories = categoryRepository.findAllByStatusIs(Status.ACTIVE.getValue());
//        List<CategoryDTO> categoryDTO = categories.stream().map(x -> new CategoryDTO(x, false, false)).collect(Collectors.toList());

        return categoryRepository.findAll(specification, PageRequest.of(page - 1, limit));
    }
}
