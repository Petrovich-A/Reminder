package by.petrovich.reminder.job.impl;

import by.petrovich.reminder.job.ReminderScheduler;
import by.petrovich.reminder.model.Reminder;
import by.petrovich.reminder.model.User;
import by.petrovich.reminder.repository.ReminderRepository;
import by.petrovich.reminder.repository.UserRepository;
import by.petrovich.reminder.service.Sender;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReminderSchedulerIml implements ReminderScheduler {
    private final ReminderRepository reminderRepository;
    private final UserRepository userRepository;
    private final Sender telegramSender;

    @Override
    @Scheduled(fixedDelayString = "${scheduling.interval}")
    public void sendReminder() {
        List<Reminder> remindersToSend = reminderRepository.findRemindersByRemindBefore(LocalDateTime.now());
        for (Reminder reminder : remindersToSend) {
            User user = reminder.getUser();
            telegramSender.send(user, reminder);
        }
    }

}
