package by.petrovich.reminder.job.impl;

import by.petrovich.reminder.job.Scheduler;
import by.petrovich.reminder.model.Reminder;
import by.petrovich.reminder.repository.ReminderRepository;
import by.petrovich.reminder.sender.Sender;
import by.petrovich.reminder.sender.message.MessageToSend;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@EnableScheduling
public class TelegramSchedulerImpl implements Scheduler<Reminder> {
    private final ReminderRepository reminderRepository;
    private final Sender<Reminder> telegramSender;

    @Override
    @Scheduled(fixedDelayString = "${scheduling.interval}")
    @Transactional(readOnly = true)
    public void executeScheduledTasks() {
        List<Reminder> remindersToSend = reminderRepository.findRemindersByRemindBefore(LocalDateTime.now());
        remindersToSend
                .stream().filter(reminder -> reminder.getUser().getTelegramUserId() != 0)
                .forEach(reminder -> {
                    MessageToSend messageToSend = telegramSender.createMessage(reminder);
                    telegramSender.sendMessage(messageToSend);
                });
    }

}
