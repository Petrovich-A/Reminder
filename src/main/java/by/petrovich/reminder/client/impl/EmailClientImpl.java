package by.petrovich.reminder.client.impl;

import by.petrovich.reminder.model.Reminder;
import by.petrovich.reminder.model.User;
import by.petrovich.reminder.client.Sender;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
@Qualifier("emailSender")
public class EmailClientImpl implements Sender {
    private static final Logger logger = LoggerFactory.getLogger(EmailClientImpl.class);

    private final JavaMailSender javaMailSender;

    @Override
    public void sendMessage(User user, Reminder reminder) {
        try {
            SimpleMailMessage simpleMailMessage = buildMailMessage(user, reminder);
            javaMailSender.send(simpleMailMessage);
        } catch (MailException e) {
            logger.error("Error sending email to User: {}. Time: {}", user.getLogin(), LocalDateTime.now(), e);
        }
        logger.info("Message successfully sent to User: {} via email. Time: {}", user.getLogin(), LocalDateTime.now());
    }

    private SimpleMailMessage buildMailMessage(User user, Reminder reminder) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(user.getEmail());
        simpleMailMessage.setSubject(reminder.getTitle());
        simpleMailMessage.setText(reminder.getDescription());
        return simpleMailMessage;
    }

}
