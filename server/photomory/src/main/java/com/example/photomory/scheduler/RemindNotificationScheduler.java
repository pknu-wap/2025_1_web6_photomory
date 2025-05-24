package com.example.photomory.scheduler;

import com.example.photomory.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RemindNotificationScheduler {

    private final NotificationService notificationService;

    // 매일 오후 7시에 실행 (cron 표현식)
    @Scheduled(cron = "0 0 19 * * *")
    public void execute() {
        notificationService.sendRemindNotification();
    }
}