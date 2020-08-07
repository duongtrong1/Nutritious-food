package com.spring.dev2chuc.nutritious_food.repository;

import com.spring.dev2chuc.nutritious_food.model.Combo;
import com.spring.dev2chuc.nutritious_food.model.Schedule;
import com.spring.dev2chuc.nutritious_food.model.ScheduleCombo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ScheduleComboRepository extends JpaRepository<ScheduleCombo, Long>, JpaSpecificationExecutor<ScheduleCombo> {
    List<ScheduleCombo> findAllByCombo(Combo combo);

    List<ScheduleCombo> findAllBySchedule(Schedule schedule);

    ScheduleCombo findByStatusAndId(Integer status, Long id);

    @Transactional
    @Modifying
    @Query(value = "alter  table schedule_combo AUTO_INCREMENT = 1 ", nativeQuery = true)
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
