package com.spring.dev2chuc.nutritious_food.service.food;

import com.spring.dev2chuc.nutritious_food.model.*;
import com.spring.dev2chuc.nutritious_food.payload.FoodRequest;
import com.spring.dev2chuc.nutritious_food.repository.FoodRepository;
import com.spring.dev2chuc.nutritious_food.service.category.CategoryService;
import com.spring.dev2chuc.nutritious_food.service.combo.ComboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FoodServiceImpl implements FoodService {

    @Autowired
    FoodRepository foodRepository;

    @Autowired
    CategoryService categoryService;

    @Autowired
    ComboService comboService;

    @Override
    public Food merge(Food food) {
        return foodRepository.save(food);
    }

    @Override
    public List<Food> findAll() {
        return foodRepository.findAllByStatus(Status.ACTIVE.getValue());
    }

    @Override
    public Food findById(Long id) {
        if (CollectionUtils.isEmpty(Collections.singleton(id))) {
            throw new RuntimeException("Null pointer exception...");
        }
        Food food = foodRepository.findByIdAndStatus(id, Status.ACTIVE.getValue());
        return food;
    }

    @Override
    public Food update(Food food, FoodRequest foodRequest) {
        if (foodRequest.getName() != null) food.setName(foodRequest.getName());
        if (foodRequest.getDescription() != null) food.setDescription(foodRequest.getDescription());
        if (foodRequest.getImage() != null) food.setImage(foodRequest.getImage());
        if (foodRequest.getPrice() != 0.0f) food.setPrice(foodRequest.getPrice());
        if (foodRequest.getCarbonhydrates() != 0.0f) food.setCarbonhydrates(foodRequest.getCarbonhydrates());
        if (foodRequest.getProtein() != 0.0f) food.setProtein(foodRequest.getProtein());
        if (foodRequest.getLipid() != 0.0f) food.setLipid(foodRequest.getLipid());
        if (foodRequest.getXenluloza() != 0.0f) food.setXenluloza(foodRequest.getXenluloza());
        if (foodRequest.getCanxi() != 0.0f) food.setCanxi(foodRequest.getCanxi());
        if (foodRequest.getIron() != 0.0f) food.setIron(foodRequest.getIron());
        if (foodRequest.getZinc() != 0.0f) food.setZinc(foodRequest.getZinc());
        if (foodRequest.getVitaminA() != 0.0f) food.setVitaminA(foodRequest.getVitaminA());
        if (foodRequest.getVitaminB() != 0.0f) food.setVitaminB(foodRequest.getVitaminB());
        if (foodRequest.getVitaminC() != 0.0f) food.setVitaminC(foodRequest.getVitaminC());
        if (foodRequest.getVitaminD() != 0.0f) food.setVitaminD(foodRequest.getVitaminD());
        if (foodRequest.getVitaminE() != 0.0f) food.setVitaminE(foodRequest.getVitaminE());
        if (foodRequest.getCalorie() != 0.0f) food.setCalorie(foodRequest.getCalorie());
        if (foodRequest.getWeight() != 0.0f) food.setWeight(foodRequest.getWeight());

        if (foodRequest.getCategoryIds().size() > 0) {
            List<Category> categories = categoryService.findAllByIdIn(foodRequest.getCategoryIds());
            Set<Category> categorySet = new HashSet<>(categories);
            food.setCategories(categorySet);
        }

        List<Combo> combos = new ArrayList<>();
        if (foodRequest.getComboIds() != null && foodRequest.getComboIds().size() > 0) {
            combos = comboService.findAllByIdIn(foodRequest.getComboIds());
            Set<Combo> comboSet = new HashSet<>(combos);
            food.setCombos(comboSet);
        }

        Food result = foodRepository.save(food);
        for (Combo combo : combos) {
            comboService.updateByListFood(combo);
        }

        return result;
    }

    @Override
    public Food merge(Food food, FoodRequest foodRequest) {
        food.setName(foodRequest.getName());
        food.setDescription(foodRequest.getDescription());
        food.setImage(foodRequest.getImage());
        food.setPrice(foodRequest.getPrice());
        food.setCarbonhydrates(foodRequest.getCarbonhydrates());
        food.setProtein(foodRequest.getProtein());
        food.setLipid(foodRequest.getLipid());
        food.setXenluloza(foodRequest.getXenluloza());
        food.setCanxi(foodRequest.getCanxi());
        food.setIron(foodRequest.getIron());
        food.setZinc(foodRequest.getZinc());
        food.setVitaminA(foodRequest.getVitaminA());
        food.setVitaminB(foodRequest.getVitaminB());
        food.setVitaminC(foodRequest.getVitaminC());
        food.setVitaminD(foodRequest.getVitaminD());
        food.setVitaminE(foodRequest.getVitaminE());
        food.setCalorie(foodRequest.getCalorie());
        food.setWeight(foodRequest.getWeight());
        food.setStatus(Status.ACTIVE.getValue());

        System.out.println(foodRequest.getCategoryIds().size());


        if (foodRequest.getCategoryIds() != null && foodRequest.getCategoryIds().size() > 0) {
            List<Category> categories = categoryService.findAllByIdIn(foodRequest.getCategoryIds());
            Set<Category> categorySet = new HashSet<>(categories);
            food.setCategories(categorySet);
        }

        List<Combo> combos = new ArrayList<>();
        if (foodRequest.getComboIds() != null && foodRequest.getComboIds().size() > 0) {
            combos = comboService.findAllByIdIn(foodRequest.getComboIds());
            Set<Combo> comboSet = new HashSet<>(combos);
            food.setCombos(comboSet);
        }

        Food result = foodRepository.save(food);
        for (Combo combo : combos) {
            comboService.updateByListFood(combo);
        }

        return result;
    }

    @Override
    public Food findByStatusAndId(Integer status, Long id) {
        if (CollectionUtils.isEmpty(Collections.singleton(status))) {
            throw new RuntimeException("Null pointer exception");
        }

        Food food = foodRepository.findByStatusAndId(status, id);
        return food;
    }

    @Override
    public List<Food> findAllByIdIn(Collection<Long> ids) {
//        if (CollectionUtils.isEmpty(ids)) {
//            throw new RuntimeException("Null pointer exception...");
//        }
        return foodRepository.findAllByIdIn(ids);
//        if (list == null) {
//            throw new RuntimeException("Null pointer exception...");
//        }
//        return list;
    }

    @Override
    public List<Food> findAllByCategory(Long categoryId) {
        Category category = categoryService.findById(categoryId);
        return foodRepository.findAllByStatusAndCategories(Status.ACTIVE.getValue(), category);
    }


    @Override
    public Page<Food> foodsWithPaginate(Specification specification, int page, int limit) {
        return foodRepository.findAll(specification, PageRequest.of(page - 1, limit));
    }

    @Override
    public List<Food> suggest(UserProfile userProfile) {
        Set<Category> categories = userProfile.getCategories();
        List<Category> categoryList = new ArrayList<>(categories);
        if (categoryList.isEmpty()) {
            categoryList = categoryService.findAll();
        }
        List<Food> foodList = foodRepository.
                findAllByStatusAndCategoriesIn(Status.ACTIVE.getValue(), categoryList)
                .stream()
                .limit(8)
                .collect(Collectors.toList());

        Set<Food> foodSet = new HashSet<>(foodList);
        List<Food> foods = new ArrayList<>(foodSet);
        System.out.println(foods.size());
        if (foods.size() <= 8) {
            int numberAdd = 8 - foods.size();
            System.out.println(numberAdd);
            List<Long> foodIds = foods.stream().map(Food::getId).collect(Collectors.toList());
            System.out.println(foodIds);
            List<Food> comboOtherList = foodRepository.findAllByIdNotInAndStatusIs(
                    foodIds,
                    Status.ACTIVE.getValue()
            ).stream().limit(numberAdd).collect(Collectors.toList());
            foods.addAll(comboOtherList);
        }
        return foods;
    }

    @Override
    public List<Food> suggestByFoodId(Long foodId) {
        Food food = foodRepository.findByStatusAndId(Status.ACTIVE.getValue(), foodId);
        if (food == null)
            return null;
        List<Category> categories = new ArrayList<>(food.getCategories());
        if (!categories.isEmpty()) {
            categories = categoryService.findAll();
        }
        return foodRepository.findAllByStatusAndCategoriesIn(Status.ACTIVE.getValue(), categories);
    }

    @Override
    public List<Food> searchByNameAndDescription(String search) {
        return foodRepository.queryAllByNameContainingAndDescriptionContaining(search, search);
    }
}
