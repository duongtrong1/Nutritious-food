package com.spring.dev2chuc.nutritious_food.service.mail;

import com.spring.dev2chuc.nutritious_food.model.Order;

public interface MailService {
    void sendMail(String mail, String subject, String text);
    String generateMailOrder(Order order);
}
