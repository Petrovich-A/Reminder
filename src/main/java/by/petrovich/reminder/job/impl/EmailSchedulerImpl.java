package by.petrovich.reminder.job.impl;

import by.petrovich.reminder.job.Scheduler;
import by.petrovich.reminder.model.Reminder;
import by.petrovich.reminder.repository.ReminderRepository;
import by.petrovich.reminder.sender.Sender;
import by.petrovich.reminder.sender.message.MessageToSend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@EnableScheduling
public class EmailSchedulerImpl implements Scheduler<Reminder> {
    private final ReminderRepository reminderRepository;
    @Qualifier("emailSenderImpl")
    private final Sender<Reminder, MessageToSend> emailSender;

    @Autowired
    public EmailSchedulerImpl(ReminderRepository reminderRepository,
                              @Qualifier("emailSenderImpl") Sender<Reminder, MessageToSend> emailSender) {
        this.reminderRepository = reminderRepository;
        this.emailSender = emailSender;
    }

    @Override
    @Scheduled(fixedDelayString = "${scheduling.interval}")
    @Transactional(readOnly = true)
    public void executeScheduledTasks() {
        List<Reminder> remindersToSend = reminderRepository.findRemindersByRemindBefore(LocalDateTime.now());

        remindersToSend.forEach(reminder -> {
            MessageToSend messageToSend = emailSender.createMessage(reminder);
            emailSender.sendMessage(messageToSend);
        });
    }

}
