package by.petrovich.reminder.job;

import org.springframework.scheduling.annotation.Scheduled;

public interface ReminderScheduler {

    @Scheduled(fixedDelayString = "${scheduling.interval}")
    void sendReminderViaTelegram();

    @Scheduled(fixedDelayString = "${scheduling.interval}")
    void sendReminderViaEmail();
}
