package by.petrovich.reminder.sender.impl;

import by.petrovich.reminder.model.Reminder;
import by.petrovich.reminder.sender.Sender;
import by.petrovich.reminder.sender.message.MessageToSend;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TelegramSender implements Sender<Reminder, MessageToSend> {
    private static final Logger logger = LoggerFactory.getLogger(TelegramSender.class);
    private final TelegramLongPollingBot bot;
    private final String MESSAGE_SUBJECT = "💡 *Don't forget about your task:*";

    @Override
    public void sendMessage(MessageToSend messageToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(messageToSend.getToRecipient());
        sendMessage.setText(messageToSend.getBody());
        sendMessage.enableMarkdown(true);
        try {
            org.telegram.telegrambots.meta.api.objects.Message sentMessage = bot.execute(sendMessage);
            if (sentMessage != null) {
                logger.info("Message successfully sent to User: {} via telegram. Time: {}", messageToSend.getToRecipient(), LocalDateTime.now());
            } else {
                logger.error("Error sending sendMessage to User: {} via Telegram.", messageToSend.getToRecipient());
            }
        } catch (TelegramApiException e) {
            logger.error("Exception occurred while sending sendMessage to User: {} via Telegram.", messageToSend.getToRecipient(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public MessageToSend createMessage(Reminder reminder) {
        return MessageToSend.builder()
                .toRecipient(String.valueOf(reminder.getUser().getTelegramUserId()))
                .subject(MESSAGE_SUBJECT)
                .body(formatBody(reminder))
                .build();
    }

    private String formatBody(Reminder reminder) {
        return String.format(
                """
                        🔔 *Reminder!*

                        *%s*

                        *Title:* %s
                        *Description:* %s
                        *Reminder Date:* %s

                        ⏰ Don't miss this event! 😃""",
                MESSAGE_SUBJECT,
                reminder.getTitle(),
                reminder.getDescription(),
                reminder.getRemind()
        );
    }
}
