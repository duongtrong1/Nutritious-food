package com.spring.dev2chuc.nutritious_food.config;

import com.spring.dev2chuc.nutritious_food.service.firebase.CronJobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

//@Component
public class ScheduledTasks {

//    @Autowired
//    CronJobService cronJobService;
//
//    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
//
//    @Scheduled(cron = "0 26 9 * * ?")
//    public void scheduleTaskWithCronToday() {
//        try {
//            cronJobService.checkUserAndDevice("morning.message", "today.message");
//        } catch (Exception ignored) {
//            logger.error("Push notification error!");
//        }
//        logger.info("Push notification success!");
//    }
//
//    @Scheduled(cron = "0 0 12 * * ?")
//    public void scheduleTaskWithCronNoonday() {
//        try {
//            cronJobService.checkUserAndDevice("noonday.message", "today.message");
//        } catch (Exception ignored) {
//            logger.error("Push notification error!");
//        }
//        logger.info("Push notification success!");
//    }
//
//    @Scheduled(cron = "0 45 12 * * ?")
//    public void scheduleTaskWithCronLastNoonday() {
//        try {
//            cronJobService.checkUserAndDevice("last.noonday.message", "today.message");
//        } catch (Exception ignored) {
//            logger.error("Push notification error!");
//        }
//        logger.info("Push notification success!");
//    }
//
//    @Scheduled(cron = "0 0 19 * * ?")
//    public void scheduleTaskWithCronDinner() {
//        try {
//            cronJobService.checkUserAndDevice("dinner.message", "today.message");
//        } catch (Exception ignored) {
//            logger.error("Push notification error!");
//        }
//        logger.info("Push notification success!");
//
//    }
//
//    @Scheduled(cron = "0 30 19 * * ?")
//    public void scheduleTaskWithCronLastDinner() {
//        try {
//            cronJobService.checkUserAndDevice("last.dinner.message", "today.message");
//        } catch (Exception ignored) {
//            logger.error("Push notification error!");
//        }
//        logger.info("Push notification success!");
//    }
}
