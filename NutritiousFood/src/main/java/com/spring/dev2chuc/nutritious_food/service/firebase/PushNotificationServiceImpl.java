package com.spring.dev2chuc.nutritious_food.service.firebase;

import org.springframework.http.HttpEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

@Service
public class PushNotificationServiceImpl {

    private static final String FIREBASE_SERVER_KEY = "AAAAAZMywAs:APA91bE22xV5JdWo5U5hR9lOo14eHPtlzTt39_JiT2NJ2cIhnxnHpEfZFyT4j_4BvBcV7W4cmpxRZRpGX5Tt37zavnwFOZFfzsy0uQHHo4ylimQQ0WXoiQjdvBPKqfFFsTk-kVWOG_ex";
    private static final String FIREBASE_API_URL = "https://fcm.googleapis.com/fcm/send";
    

    @Async
    public CompletableFuture<String> send(HttpEntity<String> entity) {

        RestTemplate restTemplate = new RestTemplate();

        /**
         https://fcm.googleapis.com/fcm/send
         Content-Type:application/json
         Content-Type:application/json
         Authorization:key=FIREBASE_SERVER_KEY*/

        ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new FCMSender("Authorization", "key=" + FIREBASE_SERVER_KEY));
        interceptors.add(new FCMSender("Content-Type", "application/json"));
        restTemplate.setInterceptors(interceptors);
        restTemplate.getMessageConverters()
                .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        String firebaseResponse = restTemplate.postForObject(FIREBASE_API_URL, entity, String.class);
        return CompletableFuture.completedFuture(firebaseResponse);
    }
}
