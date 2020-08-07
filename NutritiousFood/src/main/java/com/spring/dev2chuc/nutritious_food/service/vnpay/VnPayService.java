package com.spring.dev2chuc.nutritious_food.service.vnpay;

import com.spring.dev2chuc.nutritious_food.model.Order;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

public interface VnPayService {
    String generateURLPayment(HttpServletRequest req, Order order) throws UnsupportedEncodingException;

    String catchDataReturn(HttpServletRequest request) throws UnsupportedEncodingException;

    String catchDataIPN(HttpServletRequest request) throws UnsupportedEncodingException;
}
