package by.petrovich.reminder.client.impl;

import by.petrovich.reminder.model.Reminder;
import by.petrovich.reminder.model.User;
import by.petrovich.reminder.client.Sender;
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
    private String getTelegramApiUrl;

    @Value("${telegram.api.command}")
    private String getTelegramApiCommand;


    @Override
    public void sendMessage(User user, Reminder reminder) {
        HttpEntity<String> entity = new HttpEntity<>(buildRequestBody(user, reminder), buildHeader());
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.exchange(buildUrl(), POST, entity, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("Message successfully sent to User: {} via telegram. Time: {}", user.getLogin(), LocalDateTime.now());
            } else {
                logger.error("Error sending message via Telegram: Status Code - {}, Response Body - {}", response.getStatusCode(), response.getBody());
            }
        } catch (RestClientException e) {
            logger.error("Exception occurred while sending message to Telegram.", e);
            throw new RuntimeException(e);
        }
    }

    private String buildRequestBody(User user, Reminder reminder) {
        return String.format("{\"chat_id\":\"%s\",\"text\":\"Title: %s\\nDescription: %s\\nRemind Date: %s\"}",
                user.getChatId(), reminder.getTitle(), reminder.getDescription(), reminder.getRemind());
    }

    private HttpHeaders buildHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        return headers;
    }

    private String buildUrl() {
        return getTelegramApiUrl + botToken + getTelegramApiCommand;
    }

}
