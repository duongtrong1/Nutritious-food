package com.spring.dev2chuc.nutritious_food.controller;

import com.spring.dev2chuc.nutritious_food.model.Device;
import com.spring.dev2chuc.nutritious_food.model.User;
import com.spring.dev2chuc.nutritious_food.payload.PushNotificationRequest;
import com.spring.dev2chuc.nutritious_food.payload.SignUpRequest;
import com.spring.dev2chuc.nutritious_food.payload.response.ApiResponseError;
import com.spring.dev2chuc.nutritious_food.service.device.DeviceService;
import com.spring.dev2chuc.nutritious_food.service.firebase.CronJobService;
import com.spring.dev2chuc.nutritious_food.service.firebase.PushNotificationServiceImpl;
import com.spring.dev2chuc.nutritious_food.service.user.UserService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/push")
public class PushNotificationController {

    @Autowired
    PushNotificationServiceImpl pushNotificationService;

    @Autowired
    DeviceService deviceService;

    @Autowired
    UserService userService;

    @Autowired
    CronJobService cronJobService;

    @GetMapping("/send")
    public ResponseEntity<?> send() throws JSONException {
        List<User> users = userService.findAll();
        for (User user : users) {
            List<Device> devices = deviceService.getByUser(user);
            for (Device device : devices) {
                JSONObject body = new JSONObject();
                body.put("to", "fd6rLKcEGVQ:APA91bEDZqtHtOsEgpWUBCIztOqQ13gpK8grnvqt7M6U_NZPurpqtYiE5UZnCfvwScqxclrT6pJk365ORxNlwkfq0zwVxn7Wy2qP6y5rszLJ0fQSd8SB4WBIkZxVB9-2JhusdSTCisEp");
                body.put("collapse_key", "type_a");

                JSONObject notification = new JSONObject();
                notification.put("body", "JSA Notification");
                notification.put("title", "Happy Message!");

                body.put("notification", notification);

                HttpEntity<String> request = new HttpEntity<>(body.toString());
                CompletableFuture<String> pushNotification = pushNotificationService.send(request);
                CompletableFuture.allOf(pushNotification).join();

                try {
                    String firebaseResponse = pushNotification.get();
                    return new ResponseEntity<>(firebaseResponse, HttpStatus.OK);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }

        return new ResponseEntity<>(
                new ApiResponseError(HttpStatus.NOT_FOUND.value(), "Device not found"), HttpStatus.NOT_FOUND);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> sendToUser(@Valid @RequestBody PushNotificationRequest pushNotificationRequest, @PathVariable("id") Long id) throws JSONException {
        User user =  userService.getById(id);
        List<Device> devices = deviceService.getByUser(user);
        if (devices == null || devices.size() == 0) {
            return new ResponseEntity<>(
                    new ApiResponseError(HttpStatus.NOT_FOUND.value(), "Device not found"), HttpStatus.NOT_FOUND);
        }
        for (Device device : devices) {
            cronJobService.JsonObject(device, pushNotificationRequest.getBody(), pushNotificationRequest.getTitle(), false);
        }
        return new ResponseEntity<>(
                new ApiResponseError(HttpStatus.OK.value(), "OK"), HttpStatus.OK);
    }

    @GetMapping("/user/{id}/auto")
    public ResponseEntity<?> sendToUserAuto(@PathVariable("id") Long id) throws JSONException {
        User user =  userService.getById(id);
        List<Device> devices = deviceService.getByUser(user);
        if (devices == null || devices.size() == 0) {
            return new ResponseEntity<>(
                    new ApiResponseError(HttpStatus.NOT_FOUND.value(), "Device not found"), HttpStatus.NOT_FOUND);
        }
        String[] strings = userService.generateSuggest(user);
        for (Device device : devices) {
            cronJobService.JsonObject(device, strings[1], strings[0], false);
        }
        return new ResponseEntity<>(
                new ApiResponseError(HttpStatus.OK.value(), "OK"), HttpStatus.OK);
    }

    @GetMapping("/user/{id}/question")
    public ResponseEntity<?> sendToUserQuestion(@PathVariable("id") Long id) throws JSONException {
        User user =  userService.getById(id);
        List<Device> devices = deviceService.getByUser(user);
        if (devices == null || devices.size() == 0) {
            return new ResponseEntity<>(
                    new ApiResponseError(HttpStatus.NOT_FOUND.value(), "Device not found"), HttpStatus.NOT_FOUND);
        }
        for (Device device : devices) {
            cronJobService.JsonObject(device, "Hãy cho mình biết bạn đã ăn gì hôm này nào ^^", "Bạn đã ăn gì", true);
        }
        return new ResponseEntity<>(
                new ApiResponseError(HttpStatus.OK.value(), "OK"), HttpStatus.OK);
    }
}
