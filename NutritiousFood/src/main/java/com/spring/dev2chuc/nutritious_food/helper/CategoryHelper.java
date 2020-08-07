package com.spring.dev2chuc.nutritious_food.helper;

import com.spring.dev2chuc.nutritious_food.model.Category;

import java.util.List;

public class CategoryHelper {

    private static List<Category> resultLocal;

    public static List<Category> recusiveCategory(List<Category> data, Long parentId, String text, List<Category> result) {
        resultLocal = result;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getId() == parentId || data.get(i).getParentId() == parentId) {
                data.get(i).setName(text + data.get(i).getName());
                resultLocal.add(i, data.get(i));
                Long newParentId = data.get(i).getId();
                data.remove(i);
                for (int j = 0; j < resultLocal.size(); j++) {
                    System.out.println(resultLocal.get(j).getId() + resultLocal.get(j).getName());
                }

                recusiveCategory(data, newParentId, text + " -- ", resultLocal);
            }
        }
        return resultLocal;
    }
}
