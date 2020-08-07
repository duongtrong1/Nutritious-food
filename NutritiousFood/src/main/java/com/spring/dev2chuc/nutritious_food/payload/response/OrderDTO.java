package com.spring.dev2chuc.nutritious_food.payload.response;

import com.spring.dev2chuc.nutritious_food.helper.DateTimeHelper;
import com.spring.dev2chuc.nutritious_food.model.Order;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class OrderDTO {
    private Long id;
    private Long userId;
    private String userName;
    private String address;
    private String phone;
    private String note;
    private String code;
    private String urlPayment;
    private float totalPrice;
    private int type;
    private int status;
    private String createdAt;
    private String updatedAt;

    private Set<OrderDetailDTO> orderDetail;

    public OrderDTO(Order order, boolean hasOrderDetail) {
        this.id = order.getId();
        this.userId = order.getAddress().getUser().getId();
        this.userName = order.getAddress().getUser().getUsername();
        this.address = order.getAddress().getTitle();
        this.phone = order.getAddress().getPhone();
        this.code = order.getCode();
        this.note = order.getNote();
        this.totalPrice = order.getTotalPrice();
        this.type = order.getType();
        this.status = order.getStatus();
        if (hasOrderDetail)
            this.orderDetail = order.getOrderDetails().stream().map(orderDetail -> new OrderDetailDTO(orderDetail, false)).collect(Collectors.toSet());
        this.createdAt = DateTimeHelper.formatDateFromLong(order.getCreatedAt());
        this.updatedAt = DateTimeHelper.formatDateFromLong(order.getUpdatedAt());
    }
}
