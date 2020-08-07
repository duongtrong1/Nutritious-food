package com.spring.dev2chuc.nutritious_food.controller;

import com.spring.dev2chuc.nutritious_food.model.*;
import com.spring.dev2chuc.nutritious_food.model.audit.DateAudit;
import com.spring.dev2chuc.nutritious_food.payload.OrderRequest;
import com.spring.dev2chuc.nutritious_food.payload.response.*;
import com.spring.dev2chuc.nutritious_food.service.address.AddressService;
import com.spring.dev2chuc.nutritious_food.service.mail.MailService;
import com.spring.dev2chuc.nutritious_food.service.order.OrderService;
import com.spring.dev2chuc.nutritious_food.service.user.UserService;
import com.spring.dev2chuc.nutritious_food.service.vnpay.VnPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    UserService userService;

    @Autowired
    OrderService orderService;

    @Autowired
    VnPayService vnPayService;

    @Autowired
    AddressService addressService;

    @Autowired
    MailService mailService;



//    @GetMapping
//    public ResponseEntity<?> getListByUser() {
//        User user = userService.getUserAuth();
//        if (user == null) {
//            return new ResponseEntity<>(new ApiResponseError(HttpStatus.NOT_FOUND.value(), "User not found"), HttpStatus.NOT_FOUND);
//        } else {
//            List<OrderDTO> orderList = orderService.getAllByUser(user);
//            return new ResponseEntity<>(new ApiResponseCustom<>(HttpStatus.OK.value(), "OK", orderList), HttpStatus.OK);
//        }
//    }

    @GetMapping()
    public ResponseEntity<?> getListByUser(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "from", required = false) String from,
            @RequestParam(value = "to", required = false) String to,
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "12", required = false) int limit,
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam(value = "type", required = false) Integer type
            ) {
        User user = userService.getUserAuth();
        if (user == null) {
            return new ResponseEntity<>(new ApiResponseError(HttpStatus.NOT_FOUND.value(), "User not found"), HttpStatus.NOT_FOUND);
        }

        Specification specification = Specification.where(null);
        if (search != null && search.length() > 0) {
            specification = specification
                    .and(new SpecificationAll(new SearchCriteria("code", ":", search)))
                    .or(new SpecificationAll(new SearchCriteria("note", ":", search)));
        }
        specification = specification
                .and(new SpecificationAll(new SearchCriteria("createdAt", "orderBy", "desc")));

        if (userService.checkRoleByUser(user, RoleName.ROLE_USER)) {
            List<Address> addresses = addressService.getAllByUser(user);
            List<Order> orders = orderService.getAllByUser(user);

            Long[] orderIds = orders.stream().map(Order::getId).toArray(Long[]::new);
            if (orderIds.length == 0) {
                return new ResponseEntity<>(new ApiResponsePage<>(
                        HttpStatus.OK.value(), "OK", new Long[]{},
                        new RESTPagination(page, limit, 0, 0)), HttpStatus.OK);
            }

            specification = specification
                    .and(new SpecificationAll(new SearchCriteria("id", "in", orderIds )));
        }
        if (status != null) {
            specification = specification
                    .and(new SpecificationAll(new SearchCriteria("status", ":", status )));
        }
        if (type != null) {
            specification = specification
                    .and(new SpecificationAll(new SearchCriteria("type", ":", type )));
        }

        if (from != null && to != null) {
            List<Order> orders = orderService.getAllByCreatedAtBetween(from, to);
            Long[] foodIds;
            if (orders.size() == 0) {
                foodIds = new Long[]{Long.valueOf(0)};
            } else {
                foodIds = orders.stream().map(Order::getId).toArray(Long[]::new);
            }
            System.out.println(foodIds);

            specification = specification
                    .and(new SpecificationAll(new SearchCriteria("id", "in", foodIds)));
        }

        Page<Order> orderPage  = orderService.getAllByUserWithPaginate(specification, page, limit);
        return new ResponseEntity<>(new ApiResponsePage<>(
                HttpStatus.OK.value(), "OK", orderPage.stream()
                .map(x -> new OrderDTO(x, true))
                .collect(Collectors.toList()),
                new RESTPagination(page, limit, orderPage.getTotalPages(), orderPage.getTotalElements())), HttpStatus.OK);

    }

    @PostMapping
    public ResponseEntity<?> createOrder(@Valid @RequestBody OrderRequest orderRequest, HttpServletRequest req) throws UnsupportedEncodingException {
        User user = userService.getUserAuth();
        if (user == null) {
            return new ResponseEntity<>(new ApiResponseError(HttpStatus.NOT_FOUND.value(), "User not found"), HttpStatus.NOT_FOUND);
        } else {

            Order order = orderService.saveOrderByUser(orderRequest);
            String contentMailOrder = mailService.generateMailOrder(order);
            mailService.sendMail(user.getEmail(), "Đặt hàng thành công", contentMailOrder);
            if (order == null)
                return new ResponseEntity<>(new ApiResponseCustom<>(HttpStatus.BAD_REQUEST.value(), "Item not match"), HttpStatus.BAD_REQUEST);
            String urlVnPay = "";
            if (order.getType() == 2) {
                urlVnPay = vnPayService.generateURLPayment(req, order);
            }
            OrderDTO orderDTO = new OrderDTO(order, true);
            orderDTO.setUrlPayment(urlVnPay);
            return new ResponseEntity<>(new ApiResponseCustom<>(HttpStatus.CREATED.value(), "Save order success", orderDTO), HttpStatus.CREATED);
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getOrderDetail(@PathVariable("id") Long id) {
        User user = userService.getUserAuth();
        if (user == null) {
            return new ResponseEntity<>(new ApiResponseError(HttpStatus.NOT_FOUND.value(), "User not found"), HttpStatus.NOT_FOUND);
        } else {
            OrderDTO orderResponse = orderService.getById(id);
            if (orderResponse == null) {
                return new ResponseEntity<>(new ApiResponseError(HttpStatus.NOT_FOUND.value(), "Order not found"), HttpStatus.NOT_FOUND);
            }
            if (orderResponse.getUserId() != user.getId()) {
                return new ResponseEntity<>(new ApiResponseError(HttpStatus.BAD_REQUEST.value(), "Order not accept for you"), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(new ApiResponseCustom<>(HttpStatus.OK.value(), "OK", orderResponse), HttpStatus.OK);
        }
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable("id") Long id, @RequestParam(value = "status", required = true) Integer status) {
        User user = userService.getUserAuth();
        if (user == null) {
            return new ResponseEntity<>(new ApiResponseError(HttpStatus.NOT_FOUND.value(), "User not found"), HttpStatus.NOT_FOUND);
        } else {
            if (userService.checkRoleByUser(user, RoleName.ROLE_ADMIN)) {
                Order order = orderService.updateStatusOrder(id, status);
                if (order == null) {
                    return new ResponseEntity<>(new ApiResponseError(HttpStatus.NOT_FOUND.value(), "Order not found"), HttpStatus.NOT_FOUND);
                }
                return new ResponseEntity<>(new ApiResponseCustom<>(HttpStatus.OK.value(), "OK", new OrderDTO(order, true)), HttpStatus.OK);
            }
            return new ResponseEntity<>(new ApiResponseCustom<>(HttpStatus.UNAUTHORIZED.value(), "Request has reject"), HttpStatus.UNAUTHORIZED);
        }
    }


}
