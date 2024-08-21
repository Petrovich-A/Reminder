package by.petrovich.reminder.client.impl;

import by.petrovich.reminder.client.Sender;
import by.petrovich.reminder.model.Reminder;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

import static org.springframework.http.HttpMethod.POST;

@RequiredArgsConstructor
@Service
@Qualifier("telegramSender")
public class TelegramClientImpl implements Sender {
    private static final Logger logger = LoggerFactory.getLogger(TelegramClientImpl.class);

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.api.url}")
    private String telegramApiUrl;

    @Value("${telegram.api.command}")
    private String telegramApiCommand;

    private String TELEGRAM_API_URL;
    private static final String CHAT_ID_BODY_ELEMENT = "chat_id";

    @PostConstruct
    public void init() {
        TELEGRAM_API_URL = String.format("%s%s%s", telegramApiUrl, botToken, telegramApiCommand);
    }

    @Override
    public void sendMessage(Reminder reminder) {
        HttpEntity<String> entity = new HttpEntity<>(buildRequestBody(reminder), buildHeader());
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.exchange(TELEGRAM_API_URL, POST, entity, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("Message successfully sent to User: {} via telegram. Time: {}", reminder.getUser().getName(), LocalDateTime.now());
            } else {
                logger.error("Error sending message to User: {} via Telegram: Status Code - {}, Response Body - {}", reminder.getUser().getName(), response.getStatusCode(), response.getBody());
            }
        } catch (RestClientException e) {
            logger.error("Exception occurred while sending message to User: {} via Telegram.", reminder.getUser().getName(), e);
            throw new RuntimeException(e);
        }
    }

    private String buildRequestBody(Reminder reminder) {
        String chatIdPart = buildChatIdPart(reminder.getUser().getTelegramUserId());
        String reminderPart = buildReminderPart(reminder);
        return String.format("{%s, \"text\":\"%s\", \"parse_mode\":\"Markdown\"}", chatIdPart, reminderPart);
    }

    private String buildChatIdPart(Long telegramUserId) {
        return String.format("\"%s\":%d", CHAT_ID_BODY_ELEMENT, telegramUserId);
    }

    private String buildReminderPart(Reminder reminder) {
        return String.format("*Title:* %s\n*Description:* %s\n*Remind Date:* %s",
                reminder.getTitle(), reminder.getDescription(), reminder.getRemind());
    }

    private HttpHeaders buildHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        return headers;
    }

}
