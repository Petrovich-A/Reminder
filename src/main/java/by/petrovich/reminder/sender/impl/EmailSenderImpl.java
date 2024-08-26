package by.petrovich.reminder.sender.impl;

import by.petrovich.reminder.model.Reminder;
import by.petrovich.reminder.sender.Sender;
import by.petrovich.reminder.sender.message.MessageToSend;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
@Log4j2
public class EmailSenderImpl implements Sender<Reminder, MessageToSend> {
    public static final String EMAIL_SUBJECT = "Don't forget about your task!";
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
            log.error("Error sending email to: {}.", messageToSend.getToRecipient(), e);
        }
        log.info("Message successfully sent to email: {} via email. Time: {}", messageToSend.getToRecipient(), LocalDateTime.now());
    }

    @Override
    public MessageToSend createMessage(Reminder reminder) {
        return MessageToSend.builder()
                .toRecipient(reminder.getUser().getEmail())
                .subject(EMAIL_SUBJECT)
                .body(formatBody(reminder))
                .build();
    }

    private String formatBody(Reminder reminder) {
        return String.format(
                """
                        Reminder!

                        💡 %s

                        Title: %s
                        Description: %s
                        Reminder Date: %s

                        ⏰ Don't miss this event! 😃""",
                EMAIL_SUBJECT,
                reminder.getTitle(),
                reminder.getDescription(),
                reminder.getRemind()
        );
    }

}
