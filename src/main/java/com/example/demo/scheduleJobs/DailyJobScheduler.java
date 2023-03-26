package com.example.demo.scheduleJobs;

import com.example.demo.domain.entities.User;
import com.example.demo.services.UserService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
@EnableAsync
@Component
public class DailyJobScheduler {
    private UserService userService;

    public DailyJobScheduler(UserService userService) {
        this.userService = userService;
    }
    @Async
    @Scheduled(cron = "0 0 * * * ?")
    public void runDailyJob() {
        List<User> users = this.userService.findAll();

        users.forEach(user -> userService.dailyBonus(user));
        // Put your code here to run the daily job
        // This code will be executed once a day at midnight
    }
}
