package com.spring.dev2chuc.nutritious_food.payload.response;

import com.spring.dev2chuc.nutritious_food.helper.DateTimeHelper;
import com.spring.dev2chuc.nutritious_food.model.OrderDetail;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDetailDTO {
    private Long id;
    private Long orderId;
    private Long foodId;
    private Long comboId;
    private Long scheduleId;
    private Integer quantity;
    private Integer type;
    private float price;
    private Integer status;
    private String createdAt;
    private String updatedAt;
    private FoodDTO food;
    private ComboDTO combo;
    private ScheduleDTO schedule;
    private OrderDTO order;

    public OrderDetailDTO(OrderDetail orderDetail, boolean hasOrder) {
        this.id = orderDetail.getId();
        this.orderId = orderDetail.getOrder().getId();
        this.foodId = orderDetail.getFood() != null ? orderDetail.getFood().getId() : null;
        this.comboId = orderDetail.getCombo() != null ? orderDetail.getCombo().getId() : null;
        this.scheduleId = orderDetail.getSchedule() != null ? orderDetail.getSchedule().getId() : null;
        this.quantity = orderDetail.getQuantity();
        this.type = orderDetail.getType();
        this.price = orderDetail.getPrice();
        this.status = orderDetail.getStatus();
        this.food = orderDetail.getFood() != null ? new FoodDTO(orderDetail.getFood(), false, false) : null;
        this.combo = orderDetail.getCombo() != null ? new ComboDTO(orderDetail.getCombo(), false, false) : null;
        this.schedule = orderDetail.getSchedule() != null ? new ScheduleDTO(orderDetail.getSchedule(), false, false) : null;
        if (hasOrder) this.order = new OrderDTO(orderDetail.getOrder(), false);
        this.createdAt = DateTimeHelper.formatDateFromLong(orderDetail.getCreatedAt());
        this.updatedAt = DateTimeHelper.formatDateFromLong(orderDetail.getUpdatedAt());
    }
}
