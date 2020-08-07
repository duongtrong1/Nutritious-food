package com.spring.dev2chuc.nutritious_food.service.mail;

import com.spring.dev2chuc.nutritious_food.model.Order;
import com.spring.dev2chuc.nutritious_food.model.OrderDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailServiceIml implements  MailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sendMail(String mail, String subject, String text) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(mail);

        msg.setSubject(subject);
        msg.setText(text);

        javaMailSender.send(msg);
    }

    @Override
    public String generateMailOrder(Order order) {
        String str = "Đơn hàng của bạn \n";
        for(OrderDetail orderDetail : order.getOrderDetails()) {
            switch (orderDetail.getType()) {
                case 1:
                    str += "Món: " + orderDetail.getFood().getName() + "\n";
                    str += "- Số lượng: " + orderDetail.getQuantity() + "\n";
                    str += "- Giá: " + orderDetail.getFood().getPrice() + "VND\n\n";
                    break;
                case 2:
                    str += "Combo: " + orderDetail.getCombo().getName() + "\n";
                    str += "- Số lượng: " + orderDetail.getQuantity() + "\n";
                    str += "- Giá: " + orderDetail.getCombo().getPrice() + "VND\n\n";
                    break;
                case 3:
                    str += "Lịch trình: " + orderDetail.getSchedule().getName() + "\n";
                    str += "- Số lượng: " + orderDetail.getQuantity() + "\n";
                    str += "- Giá: " + orderDetail.getSchedule().getPrice() + " VND\n\n";
                    break;
                default:
                    break;
            }
        }
        return str += "\n\nTổng giá: " + order.getTotalPrice() + " VND";
    }
}
