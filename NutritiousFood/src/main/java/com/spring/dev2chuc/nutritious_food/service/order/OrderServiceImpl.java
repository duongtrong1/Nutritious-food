package com.spring.dev2chuc.nutritious_food.service.order;

import com.spring.dev2chuc.nutritious_food.config.VnPayConfig;
import com.spring.dev2chuc.nutritious_food.model.*;
import com.spring.dev2chuc.nutritious_food.model.audit.DateAudit;
import com.spring.dev2chuc.nutritious_food.payload.OrderDetailRequest;
import com.spring.dev2chuc.nutritious_food.payload.OrderRequest;
import com.spring.dev2chuc.nutritious_food.payload.response.OrderDTO;
import com.spring.dev2chuc.nutritious_food.payload.response.OrderDetailDTO;
import com.spring.dev2chuc.nutritious_food.repository.*;
import com.spring.dev2chuc.nutritious_food.service.history.HistoryService;
import com.spring.dev2chuc.nutritious_food.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserService userService;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    ComboRepository comboRepository;

    @Autowired
    FoodRepository foodRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    HistoryService historyService;

    @Autowired
    OrderDetailRepository orderDetailRepository;

    @Override
    public List<Order> getAllByUser(User user) {
        List<Address> addresses;
        if (userService.checkRoleByUser(user, RoleName.ROLE_ADMIN)) {
            return orderRepository.findAll();
        } else {
            addresses = addressRepository.findAllByUserAndStatus(user, Status.ACTIVE.getValue());
            return orderRepository.findAllByAddressIn(addresses);
        }

    }

    @Override
    public Page<Order> getAllByUserWithPaginate(Specification specification, int page, int limit) {
        return orderRepository.findAll(specification, PageRequest.of(page - 1, limit));
    }

    @Override
    public Order saveOrderByUser(OrderRequest orderRequest) {
        System.out.println(orderRequest.getAddressId());
        Address address = addressRepository.findByIdAndStatus(orderRequest.getAddressId(), Status.ACTIVE.getValue());
        if (address == null) return null;
        Order order = new Order(address, (float) 0, orderRequest.getNote(), orderRequest.getType());
        order.setCode(VnPayConfig.getRandomNumber(8));
        Order orderSave = orderRepository.save(order);
        float totalPrice = 0;

        Set<OrderDetail> orderDetails = new HashSet<>();
        for (OrderDetailRequest orderDetailRequest : orderRequest.getOrderDetails()) {
            OrderDetail orderDetailCurrent = null;
            if (orderDetailRequest.getFoodId() != null) {
                Food food = foodRepository.findByIdAndStatus(orderDetailRequest.getFoodId(), Status.ACTIVE.getValue());
                if (food == null) return null;
                orderDetailCurrent = new OrderDetail(
                        orderSave,
                        food,
                        orderDetailRequest.getQuantity(),
                        food.getPrice()
                );
                History history = new History(food.getCalorie() * orderDetailRequest.getQuantity(),
                        "Đã đặt hàng food",
                        generateTypeNow(),
                        address.getUser(),
                        food
                );
                historyService.save(history);
            } else if (orderDetailRequest.getComboId() != null) {
                Combo combo = comboRepository.findByStatusAndId(Status.ACTIVE.getValue(), orderDetailRequest.getComboId());
                if (combo == null) return null;
                orderDetailCurrent = new OrderDetail(
                        orderSave,
                        combo,
                        orderDetailRequest.getQuantity(),
                        combo.getPrice()
                );
                for (Food food : combo.getFoods()) {
                    History history = new History(food.getCalorie() * orderDetailRequest.getQuantity(),
                            "Đã đặt hàng combo",
                            generateTypeNow(),
                            address.getUser(),
                            food
                    );
                    historyService.save(history);
                }
            } else if (orderDetailRequest.getScheduleId() != null) {
                Schedule schedule = scheduleRepository.findByStatusAndId(Status.ACTIVE.getValue(), orderDetailRequest.getScheduleId());
                if (schedule == null) return null;
                orderDetailCurrent = new OrderDetail(
                        orderSave,
                        schedule,
                        orderDetailRequest.getQuantity(),
                        schedule.getPrice()
                );
            } else {
                return null;
            }

            OrderDetail orderDetail = orderDetailRepository.save(orderDetailCurrent);

            orderDetails.add(orderDetail);
            System.out.println(orderDetail.getType());
            totalPrice += orderDetailCurrent.getPrice() * orderDetailRequest.getQuantity();
        }
        orderSave.setTotalPrice(totalPrice);
        orderSave.setOrderDetails(orderDetails);
        return orderRepository.save(orderSave);
    }

    private Integer generateTypeNow() {
        Calendar rightNow = Calendar.getInstance();
        int hour = rightNow.get(Calendar.HOUR_OF_DAY);
        System.out.println(hour);
        if (hour >= 5 && hour < 11) {
            return 1;
        } else if (hour >= 16 || hour < 5) {
            return 3;
        } else {
            return 2;
        }
    }

    @Override
    public Order updateStatusOrder(Long id, Integer status) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            if (order.getStatus() <= 5)
                order.setStatus(status);
            orderRepository.save(order);
            return order;
        }
        return null;
    }

    @Override
    public OrderDTO getById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(null);
        if (order == null) {
            return null;
        }

        List<OrderDetail> orderDetails = orderDetailRepository.findAllByOrderAndStatus(order, Status.ACTIVE.getValue());
        Set<OrderDetailDTO> orderDetailDTOS = new HashSet<>();
        for (OrderDetail orderDetail : orderDetails) {
            OrderDetailDTO onlyOrderDetailResponse = new OrderDetailDTO(orderDetail, true);
            orderDetailDTOS.add(onlyOrderDetailResponse);
        }

        return new OrderDTO(order, true);
    }

    @Override
    public List<Order> getAllByCreatedAtBetween(String from, String to) {
        Instant fromInstant = DateAudit.stringToInstant(from);
        Instant toInstant = DateAudit.stringToInstant(to);
        return orderRepository.findAllByCreatedAtBetween(fromInstant, toInstant);
    }
}
