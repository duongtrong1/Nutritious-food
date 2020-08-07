package com.spring.dev2chuc.nutritious_food.service.combo;

import com.spring.dev2chuc.nutritious_food.model.*;
import com.spring.dev2chuc.nutritious_food.payload.ComboRequest;
import com.spring.dev2chuc.nutritious_food.repository.ComboRepository;
import com.spring.dev2chuc.nutritious_food.service.category.CategoryService;
import com.spring.dev2chuc.nutritious_food.service.food.FoodService;
import com.spring.dev2chuc.nutritious_food.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ComboServiceImpl implements ComboService {

    @Autowired
    ComboRepository comboRepository;

    @Autowired
    CategoryService categoryService;

    @Autowired
    FoodService foodService;

    @Autowired
    UserService userService;

    @Override
    public Combo findById(Long id) {
        if (CollectionUtils.isEmpty(Collections.singleton(id))) {
            throw new NullPointerException("Null pointer exception");
        }
        return comboRepository.findByStatusAndId(Status.ACTIVE.getValue(), id);
    }

    @Override
    public List<Combo> findAll() {
        return comboRepository.findAllByStatus(Status.ACTIVE.getValue());
    }

    @Override
    public Combo merge(Combo combo) {
        return comboRepository.save(combo);
    }

    @Override
    public Combo update(Combo combo, ComboRequest comboRequest) {
        if (comboRequest.getName() != null) combo.setName(comboRequest.getName());
        if (comboRequest.getDescription() != null) combo.setDescription(comboRequest.getDescription());
        if (comboRequest.getImage() != null) combo.setImage(comboRequest.getImage());
        if (comboRequest.getPrice() != 0.0f) combo.setPrice(comboRequest.getPrice());
        if (comboRequest.getCarbonhydrates() != 0.0f) combo.setCarbonhydrates(comboRequest.getCarbonhydrates());
        if (comboRequest.getProtein() != 0.0f) combo.setProtein(comboRequest.getProtein());
        if (comboRequest.getLipid() != 0.0f) combo.setLipid(comboRequest.getLipid());
        if (comboRequest.getXenluloza() != 0.0f) combo.setXenluloza(comboRequest.getXenluloza());
        if (comboRequest.getCanxi() != 0.0f) combo.setCanxi(comboRequest.getCanxi());
        if (comboRequest.getIron() != 0.0f) combo.setIron(comboRequest.getIron());
        if (comboRequest.getZinc() != 0.0f) combo.setZinc(comboRequest.getZinc());
        if (comboRequest.getVitaminA() != 0.0f) combo.setVitaminA(comboRequest.getVitaminA());
        if (comboRequest.getVitaminB() != 0.0f) combo.setVitaminB(comboRequest.getVitaminB());
        if (comboRequest.getVitaminC() != 0.0f) combo.setVitaminC(comboRequest.getVitaminC());
        if (comboRequest.getVitaminD() != 0.0f) combo.setVitaminD(comboRequest.getVitaminD());
        if (comboRequest.getVitaminE() != 0.0f) combo.setVitaminE(comboRequest.getVitaminE());
        if (comboRequest.getCalorie() != 0.0f) combo.setCalorie(comboRequest.getCalorie());
        if (comboRequest.getWeight() != 0.0f) combo.setWeight(comboRequest.getWeight());

        if (comboRequest.getCategoryIds() != null && comboRequest.getCategoryIds().size() > 0) {
            List<Category> categories = categoryService.findAllByIdIn(comboRequest.getCategoryIds());
            Set<Category> categorySet = new HashSet<>(categories);
            combo.setCategories(categorySet);
            System.out.println(combo.getCategories().size());
        }

        if (comboRequest.getFoodIds() != null && comboRequest.getFoodIds().size() > 0) {
            List<Food> foodList = foodService.findAllByIdIn(comboRequest.getFoodIds());
            Set<Food> foodSet = new HashSet<>(foodList);
            combo.setFoods(foodSet);
        }

        updateByListFood(combo);
        return comboRepository.save(combo);
    }

    @Override
    public void updateByListFood(Combo combo) {
        combo.setCalorie((float) combo.getFoods().stream().filter(o -> o.getCalorie() > 0).mapToDouble(Food::getCalorie).sum());
        combo.setCarbonhydrates((float) combo.getFoods().stream().filter(o -> o.getCarbonhydrates() > 0).mapToDouble(Food::getCarbonhydrates).sum());
        combo.setProtein((float) combo.getFoods().stream().filter(o -> o.getProtein() > 0).mapToDouble(Food::getProtein).sum());
        combo.setLipid((float) combo.getFoods().stream().filter(o -> o.getLipid() > 0).mapToDouble(Food::getLipid).sum());
        combo.setXenluloza((float) combo.getFoods().stream().filter(o -> o.getXenluloza() > 0).mapToDouble(Food::getXenluloza).sum());
        combo.setCanxi((float) combo.getFoods().stream().filter(o -> o.getCanxi() > 0).mapToDouble(Food::getCanxi).sum());
        combo.setIron((float) combo.getFoods().stream().filter(o -> o.getIron() > 0).mapToDouble(Food::getIron).sum());
        combo.setZinc((float) combo.getFoods().stream().filter(o -> o.getZinc() > 0).mapToDouble(Food::getZinc).sum());
        combo.setVitaminA((float) combo.getFoods().stream().filter(o -> o.getVitaminA() > 0).mapToDouble(Food::getVitaminA).sum());
        combo.setVitaminB((float) combo.getFoods().stream().filter(o -> o.getVitaminB() > 0).mapToDouble(Food::getVitaminB).sum());
        combo.setVitaminC((float) combo.getFoods().stream().filter(o -> o.getVitaminC() > 0).mapToDouble(Food::getVitaminC).sum());
        combo.setVitaminD((float) combo.getFoods().stream().filter(o -> o.getVitaminD() > 0).mapToDouble(Food::getVitaminD).sum());
        combo.setVitaminE((float) combo.getFoods().stream().filter(o -> o.getVitaminE() > 0).mapToDouble(Food::getVitaminE).sum());
        combo.setWeight((float) combo.getFoods().stream().filter(o -> o.getWeight() > 0).mapToDouble(Food::getWeight).sum());
        comboRepository.save(combo);
    }

    @Override
    public List<Combo> suggest(UserProfile userProfile) {
        Set<Category> categories = userProfile.getCategories();
        List<Category> categoryList = new ArrayList<>(categories);
        System.out.println(categoryList.size());
        if (categoryList.isEmpty()) {
            categoryList = categoryService.findAll();
        }
        float calories = (float) userProfile.getCaloriesConsumed() / 3;
        List<Combo> comboList = comboRepository
                .findAllByStatusAndCategoriesInAndCalorieBetween(
                        Status.ACTIVE.getValue(),
                        categoryList,
                        calories - 100,
                        calories + 100)
                .stream()
                .limit(8)
                .collect(Collectors.toList());
        Set<Combo> comboSet = new HashSet<>(comboList);
        List<Combo> combos = new ArrayList<>(comboSet);
        System.out.println(combos.size());
        if (combos.size() <= 8) {
            int numberAdd = 8 - combos.size();
            List<Long> comboIds = combos.stream().map(Combo::getId).collect(Collectors.toList());
            List<Combo> comboOtherList = comboRepository.findAllByIdNotInAndStatusIsAndCalorieBetween(
                    comboIds,
                    Status.ACTIVE.getValue(),
                    calories - 100,
                    calories + 100
            ).stream().limit(numberAdd).collect(Collectors.toList());
            combos.addAll(comboOtherList);
            System.out.println(combos.size());
        }
        return combos;
    }

    @Override
    public List<Combo> findAllByCategory(Long categoryId) {
        Category category = categoryService.findById(categoryId);
        return comboRepository.findAllByCategoriesAndStatus(category, Status.ACTIVE.getValue());
    }

    @Override
    public Combo merge(Combo combo, ComboRequest comboRequest) {
        combo.setName(comboRequest.getName());
        combo.setDescription(comboRequest.getDescription());
        combo.setImage(comboRequest.getImage());
        combo.setCarbonhydrates(comboRequest.getCarbonhydrates());
        combo.setProtein(comboRequest.getProtein());
        combo.setLipid(comboRequest.getLipid());
        combo.setXenluloza(comboRequest.getXenluloza());
        combo.setCanxi(comboRequest.getCanxi());
        combo.setIron(comboRequest.getIron());
        combo.setZinc(comboRequest.getZinc());
        combo.setVitaminA(comboRequest.getVitaminA());
        combo.setVitaminB(comboRequest.getVitaminB());
        combo.setVitaminC(comboRequest.getVitaminC());
        combo.setVitaminD(comboRequest.getVitaminD());
        combo.setVitaminE(comboRequest.getVitaminE());
        combo.setCalorie(comboRequest.getCalorie());
        combo.setWeight(comboRequest.getWeight());
        combo.setStatus(Status.ACTIVE.getValue());


        List<Long> categoryIds = comboRequest.getCategoryIds() == null ? new ArrayList<Long>() : comboRequest.getCategoryIds();
        List<Category> categories = categoryService.findAllByIdIn(categoryIds);
        Set<Category> categorySet = new HashSet<>(categories);
        combo.setCategories(categorySet);

        List<Long> foodIds = comboRequest.getFoodIds() == null ? new ArrayList<Long>() : comboRequest.getFoodIds();
        List<Food> foodList = foodService.findAllByIdIn(foodIds);
        Set<Food> foodSet = new HashSet<>(foodList);
        combo.setFoods(foodSet);

        System.out.println(comboRequest.getPrice());
        if (comboRequest.getPrice() != 0) {
            combo.setPrice(comboRequest.getPrice());
        } else {
            float priceCombo = (float) 0;
            for (Food food : foodList) {
                priceCombo += food.getPrice();
            }
            combo.setPrice(priceCombo);
        }
        Combo result = comboRepository.save(combo);
        updateByListFood(result);
        return result;
    }

    @Override
    public Combo findByStatusAndId(Integer status, Long id) {
        return comboRepository.findByStatusAndId(status, id);
    }


    @Override
    public Page<Combo> combosWithPaginate(Specification specification, int page, int limit) {
        return comboRepository.findAll(specification, PageRequest.of(page - 1, limit));
    }

    @Override
    public List<Combo> findAllByIdIn(List<Long> comboIds) {
        return comboRepository.findAllByStatusAndIdIn(Status.ACTIVE.getValue(), comboIds);
    }
}
