package by.petrovich.reminder.client.impl;

import by.petrovich.reminder.client.Sender;
import by.petrovich.reminder.model.Reminder;
import by.petrovich.reminder.model.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${reminder.email.registration.subject}")
    private String registrationSubject;

    @Value("${reminder.email.registration.text}")
    private String registrationText;

    @Value("${telegram.bot.link}")
    private String linkToTelegramBot;

    private final JavaMailSender javaMailSender;

    @Override
    public void sendMessage(Reminder reminder) {
        try {
            SimpleMailMessage simpleMailMessage = buildMailMessage(reminder.getUser().getEmail(), reminder.getTitle(), reminder.getDescription());
            javaMailSender.send(simpleMailMessage);
        } catch (MailException e) {
            logger.error("Error sending email to User: {}. Time: {}", reminder.getUser().getName(), LocalDateTime.now(), e);
        }
        logger.info("Message successfully sent to User: {} via email. Time: {}", reminder.getUser().getName(), LocalDateTime.now());
    }


    public void sendRegistrationMessage(User user) {
        try {
            String prepareRegistrationText = prepareRegistrationText(user);
            SimpleMailMessage simpleMailMessage = buildMailMessage(user.getEmail(), registrationSubject, prepareRegistrationText);
            javaMailSender.send(simpleMailMessage);
        } catch (MailException e) {
            logger.error("Error sending email to User: {}. Time: {}", user.getName(), LocalDateTime.now(), e);
        }
        logger.info("Message successfully sent to User: {} via email. Time: {}", user.getName(), LocalDateTime.now());
    }

    private String buildTelegramLink(Long userId) {
        return String.format("%s?start=%s", linkToTelegramBot, userId);
    }

    private String generateTokenForUser(User user) {
        return user.getId().toString();
    }

    private String prepareRegistrationText(User user) {
        String link = buildTelegramLink(user.getId());
        return registrationText
                .replace("USER_NAME", user.getName())
                .replace("LINK_TO_TELEGRAM_BOT_WITH_ENCRYPTED_USER_ID", link);
    }

    private SimpleMailMessage buildMailMessage(String recipient, String subject, String text) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(recipient);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(text);
        return simpleMailMessage;
    }

}
