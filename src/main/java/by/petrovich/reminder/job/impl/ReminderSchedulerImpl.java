package by.petrovich.reminder.job.impl;

import by.petrovich.reminder.client.Sender;
import by.petrovich.reminder.job.ReminderScheduler;
import by.petrovich.reminder.model.Reminder;
import by.petrovich.reminder.repository.ReminderRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
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
    @Transactional(readOnly = true)
    public void sendReminderViaTelegram() {
        List<Reminder> remindersToSend = findAllRemindersBeforeNow().stream()
                .filter(reminder -> reminder.getUser().getTelegramUserId() != 0)
                .collect(Collectors.toList());
        sendReminders(remindersToSend, telegramSender);
    }

    @Override
    @Scheduled(fixedDelayString = "${scheduling.interval}")
    @Transactional(readOnly = true)
    public void sendReminderViaEmail() {
        List<Reminder> remindersToSend = findAllRemindersBeforeNow();
        sendReminders(remindersToSend, emailSender);
    }

    private List<Reminder> findAllRemindersBeforeNow() {
        return reminderRepository.findRemindersByRemindBefore(LocalDateTime.now());
    }

    private void sendReminders(List<Reminder> reminders, Sender sender) {
        reminders.forEach(sender::sendMessage);
    }

}
