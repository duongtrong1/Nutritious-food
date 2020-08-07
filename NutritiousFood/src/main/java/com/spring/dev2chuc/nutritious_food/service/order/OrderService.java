package com.spring.dev2chuc.nutritious_food.service.order;

import com.spring.dev2chuc.nutritious_food.model.Order;
import com.spring.dev2chuc.nutritious_food.model.User;
import com.spring.dev2chuc.nutritious_food.payload.OrderRequest;
import com.spring.dev2chuc.nutritious_food.payload.response.OrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface OrderService {
    List<Order> getAllByUser(User user);

    Page<Order> getAllByUserWithPaginate(Specification specification, int page, int limit);

    Order saveOrderByUser(OrderRequest orderRequest);

    Order updateStatusOrder(Long id, Integer status);

    OrderDTO getById(Long id);

    List<Order> getAllByCreatedAtBetween(String from, String to);


}
