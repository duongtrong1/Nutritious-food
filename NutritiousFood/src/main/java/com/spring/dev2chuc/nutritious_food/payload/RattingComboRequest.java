package com.spring.dev2chuc.nutritious_food.payload;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
public class RattingComboRequest {
    private Integer rate;
    @Column(columnDefinition = "TEXT")
    private String comment;
    private String image;

    private Long userId;
    private Long comboId;
}
