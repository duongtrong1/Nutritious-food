package com.spring.dev2chuc.nutritious_food.payload.response;

import com.spring.dev2chuc.nutritious_food.helper.DateTimeHelper;
import com.spring.dev2chuc.nutritious_food.model.History;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class HistoryDTO {
    private Long id;
    private float calorie;
    private String comment;
    private int type;
    private int status;
    private String createdAt;
    private String updatedAt;
    private UserDTO user;
    private FoodDTO food;


    public HistoryDTO(History history, boolean hasUser, boolean hasFood) {
        this.id = history.getId();
        this.calorie = history.getCalorie();
        this.comment = history.getComment();
        this.type = history.getType();
        this.status = history.getStatus();
        if (hasUser)
            this.user = new UserDTO(history.getUser(), false,false,false,false,false);
        if (hasFood)
            this.food = new FoodDTO(history.getFood(), false, false);
        this.createdAt = DateTimeHelper.formatDateFromLong(history.getCreatedAt());
        this.updatedAt = DateTimeHelper.formatDateFromLong(history.getUpdatedAt());
    }
}
