package by.petrovich.reminder.sender.impl;

import by.petrovich.reminder.model.User;
import by.petrovich.reminder.sender.Sender;
import by.petrovich.reminder.sender.message.MessageToSend;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static by.petrovich.reminder.utils.EncodingUtils.encodeBase64;

@RequiredArgsConstructor
@Component
public class RegistrationEmailSenderImpl implements Sender<User> {
    private static final Logger logger = LoggerFactory.getLogger(RegistrationEmailSenderImpl.class);

    @Value("${reminder.email.registration.subject}")
    private String registrationSubject;

    @Value("${reminder.email.registration.text}")
    private String registrationText;

    @Value("${telegram.bot.link}")
    private String linkToTelegramBot;

    private final JavaMailSender javaMailSender;

    @Override
    public void sendMessage(MessageToSend messageToSend) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setTo(messageToSend.getToRecipient());
            simpleMailMessage.setSubject(messageToSend.getSubject());
            simpleMailMessage.setText(messageToSend.getBody());
            javaMailSender.send(simpleMailMessage);
        } catch (MailException e) {
            logger.error("Error sending email to User: {}. Time: {}", messageToSend.getToRecipient(), LocalDateTime.now(), e);
        }
        logger.info("Message successfully sent to User: {} via email. Time: {}", messageToSend.getToRecipient(), LocalDateTime.now());
    }

    @Override
    public MessageToSend createMessage(User user) {
        String link = prepareTelegramLinkWithUserId(user.getId());
        String body = registrationText
                .replace("USER_NAME", user.getName())
                .replace("LINK_TO_TELEGRAM_BOT_WITH_ENCRYPTED_USER_ID", link);

        return MessageToSend.builder()
                .toRecipient(user.getEmail())
                .subject(registrationSubject)
                .body(body)
                .build();
    }

    private String prepareTelegramLinkWithUserId(Long userId) {
        return String.format("%s?start=%s", linkToTelegramBot, encodeBase64(userId.toString()));
    }

    private String prepareRegistrationBody(User user) {
        String link = prepareTelegramLinkWithUserId(user.getId());
        return registrationText
                .replace("USER_NAME", user.getName())
                .replace("LINK_TO_TELEGRAM_BOT_WITH_ENCRYPTED_USER_ID", link);
    }

    private String generateTokenForUser(User user) {
        return user.getId().toString();
    }

}
