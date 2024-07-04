package by.petrovich.reminder.service.impl;

import by.petrovich.reminder.model.Reminder;
import by.petrovich.reminder.model.User;
import by.petrovich.reminder.repository.ReminderRepository;
import by.petrovich.reminder.service.ReminderScheduler;
import by.petrovich.reminder.service.Sender;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReminderSchedulerIml implements ReminderScheduler {
    private final ReminderRepository reminderRepository;
    private final Sender telegramSender;

    @Override
    @Scheduled(fixedRate = 60000)
    public void sendReminder() {
        List<Reminder> remindersToSend = reminderRepository.findRemindersByRemindBefore(LocalDateTime.now());

        // TODO: 04.07.2024
        // User user = reminder.getUser();
        User user = new User("495137766");

        for (Reminder reminder : remindersToSend) {
            telegramSender.send(user, reminder);
        }
    }

}
