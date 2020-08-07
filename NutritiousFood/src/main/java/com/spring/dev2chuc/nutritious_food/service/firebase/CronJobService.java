package com.spring.dev2chuc.nutritious_food.service.firebase;

import com.spring.dev2chuc.nutritious_food.model.Device;
import com.spring.dev2chuc.nutritious_food.model.User;
import com.spring.dev2chuc.nutritious_food.service.device.DeviceService;
import com.spring.dev2chuc.nutritious_food.service.user.UserService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class CronJobService {

    @Autowired
    DeviceService deviceService;

    @Autowired
    UserService userService;

    @Autowired
    DefineMessage message;

    @Autowired
    PushNotificationServiceImpl pushNotificationService;

    public String JsonObject(Device device, String body, String title, Boolean question) throws JSONException {
        JSONObject data = new JSONObject();
        data.put("to", device.getId());
        data.put("collapse_key", "type_a");

        JSONObject notification = new JSONObject();
        notification.put("body", body);
        notification.put("title", title);

        data.put("notification", notification);
        if (question) {
            JSONObject type = new JSONObject();
            type.put("type", "question");
            data.put("data", type);
        }
        HttpEntity<String> request = new HttpEntity<>(data.toString());
        CompletableFuture<String> pushNotification = pushNotificationService.send(request);
        CompletableFuture.allOf(pushNotification).join();

        try {
            String firebaseResponse = pushNotification.get();
            return firebaseResponse;
        } catch (InterruptedException e) {
            return e.getMessage().toString();
        } catch (ExecutionException e) {
            return e.getMessage().toString();
        }
    }

    public void checkUserAndDevice(String body, String title) {
        List<User> users = userService.findAll();
        for (User user : users) {
            List<Device> devices = deviceService.getByUser(user);
            for (Device device : devices) {
                try {
                    JsonObject(device, body, title, false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
