package by.petrovich.reminder.sender.impl;

import by.petrovich.reminder.model.Reminder;
import by.petrovich.reminder.sender.Sender;
import by.petrovich.reminder.sender.message.MessageToSend;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;

@Component
@Log4j2
public class TelegramSender implements Sender<Reminder, MessageToSend> {
    public static final String REMINDER_TELEGRAM_TEMPLATE = """
            🔔 *Reminder!*

            *%s*

            *Title:* %s
            *Description:* %s
            *Reminder Date:* %s

            ⏰ Don't miss this event! 😃""";
    private final TelegramLongPollingBot bot;
    private final String MESSAGE_SUBJECT = "💡 *Don't forget about your task:*";

    public TelegramSender(TelegramLongPollingBot bot) {
        this.bot = bot;
    }

    @Override
    public void sendMessage(MessageToSend messageToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(messageToSend.getToRecipient());
        sendMessage.setText(messageToSend.getBody());
        sendMessage.enableMarkdown(true);
        try {
            org.telegram.telegrambots.meta.api.objects.Message sentMessage = bot.execute(sendMessage);
            if (sentMessage != null) {
                log.info("Message successfully sent to User: {} via telegram. Time: {}", messageToSend.getToRecipient(), LocalDateTime.now());
            } else {
                log.error("Error sending sendMessage to User: {} via Telegram.", messageToSend.getToRecipient());
            }
        } catch (TelegramApiException e) {
            log.error("Exception occurred while sending sendMessage to User: {} via Telegram.", messageToSend.getToRecipient(), e);
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
                REMINDER_TELEGRAM_TEMPLATE,
                MESSAGE_SUBJECT,
                reminder.getTitle(),
                reminder.getDescription(),
                reminder.getRemind()
        );
    }
}
