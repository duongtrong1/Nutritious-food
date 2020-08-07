package com.spring.dev2chuc.nutritious_food.repository;

import com.spring.dev2chuc.nutritious_food.model.Category;
import com.spring.dev2chuc.nutritious_food.model.Combo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ComboRepository extends JpaRepository<Combo, Long>, JpaSpecificationExecutor<Combo> {
    Combo findByStatusAndId(Integer status, Long id);

    List<Combo> findAllByStatus(int Status);

    List<Combo> findAllByCategoriesAndStatus(Category category, Integer status);

    List<Combo> findAllByIdNotInAndStatusIsAndCalorieBetween(List<Long> ids, Integer status, float minCalories, float maxCalories);

    List<Combo> findAllByStatusAndIdIn(Integer status, List<Long> ids);

    List<Combo> findAllByStatusAndCategoriesInAndCalorieBetween(Integer status, List<Category> categories, float minCalories, float maxCalories);

    @Transactional
    @Modifying
    @Query(value = "alter  table combos AUTO_INCREMENT = 1 ", nativeQuery = true)
    void resetIncrement();

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "SET FOREIGN_KEY_CHECKS=0;")
    void disableForeignKeyCheck();

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "SET FOREIGN_KEY_CHECKS=1;")
    void enableForeignKeyCheck();
}
