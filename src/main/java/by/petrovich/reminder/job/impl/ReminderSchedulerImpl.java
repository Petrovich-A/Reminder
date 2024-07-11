package by.petrovich.reminder.job.impl;

import by.petrovich.reminder.job.ReminderScheduler;
import by.petrovich.reminder.model.Reminder;
import by.petrovich.reminder.model.User;
import by.petrovich.reminder.repository.ReminderRepository;
import by.petrovich.reminder.client.Sender;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ReminderSchedulerImpl implements ReminderScheduler {
    private final ReminderRepository reminderRepository;
    private final Sender telegramSender;
    private final Sender emailSender;

    public ReminderSchedulerImpl(ReminderRepository reminderRepository,
                                 @Qualifier("telegramSender") Sender telegramSender,
                                 @Qualifier("emailSender") Sender emailSender) {
        this.reminderRepository = reminderRepository;
        this.telegramSender = telegramSender;
        this.emailSender = emailSender;
    }

    @Override
    @Scheduled(fixedDelayString = "${scheduling.interval}")
    public void sendReminderViaTelegram() {
        sendReminders(telegramSender);
    }

    @Override
    @Scheduled(fixedDelayString = "${scheduling.interval}")
    public void sendReminderViaEmail() {
        sendReminders(emailSender);
    }

    private void sendReminders(Sender sender) {
        List<Reminder> remindersToSend = reminderRepository.findRemindersByRemindBefore(LocalDateTime.now());
        for (Reminder reminder : remindersToSend) {
            User user = reminder.getUser();
            sender.sendMessage(user, reminder);
        }
    }

}
