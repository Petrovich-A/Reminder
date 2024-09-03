package by.petrovich.reminder.sender.impl;

import by.petrovich.reminder.model.User;
import by.petrovich.reminder.sender.Sender;
import by.petrovich.reminder.sender.message.RegistrationMessageToSend;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static by.petrovich.reminder.utils.EncodingUtils.encodeBase64;

@RequiredArgsConstructor
@Component
@Log4j2
public class RegistrationEmailSenderImpl implements Sender<User, RegistrationMessageToSend> {

    @Value("${reminder.email.registration.subject}")
    private String registrationSubject;

    @Value("${reminder.email.registration.text}")
    private String registrationText;

    @Value("${telegram.bot.link}")
    private String linkToTelegramBot;

    private final JavaMailSender javaMailSender;

    @Override
    public void sendMessage(RegistrationMessageToSend registrationMessageToSend) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setTo(registrationMessageToSend.getToRecipient());
            mimeMessageHelper.setSubject(registrationMessageToSend.getSubject());
            mimeMessageHelper.setText(registrationMessageToSend.getBody());

            Resource resource = new ClassPathResource(registrationMessageToSend.getAttachment());
            if (resource.exists()) {
                mimeMessageHelper.addAttachment("logo_reminder.png", resource);
            } else {
                log.error("Attachment file not found: {}", registrationMessageToSend.getAttachment());
            }

            javaMailSender.send(mimeMessage);
            log.info("Message successfully sent to User: {} via email. Time: {}",
                    registrationMessageToSend.getToRecipient(), LocalDateTime.now());
        } catch (MessagingException e) {
            log.error("Error sending email to User: {}.", registrationMessageToSend.getToRecipient(), e);
        }
    }

    @Override
    public RegistrationMessageToSend createMessage(User user) {
        String logoPath = "static/logo_reminder.png";
        return RegistrationMessageToSend.builder()
                .toRecipient(user.getEmail())
                .subject(registrationSubject)
                .body(prepareRegistrationBody(user))
                .attachment(logoPath)
                .build();
    }

    private String prepareRegistrationBody(User user) {
        String link = prepareTelegramLinkWithUserId(user.getId());
        return registrationText
                .replace("USER_NAME", user.getName())
                .replace("LINK_TO_TELEGRAM_BOT_WITH_ENCRYPTED_USER_ID", link);
    }

    private String prepareTelegramLinkWithUserId(Long userId) {
        return String.format("%s?start=%s", linkToTelegramBot, encodeBase64(userId.toString()));
    }

}
