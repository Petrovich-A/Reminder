package by.petrovich.reminder.job.telegram;

import by.petrovich.reminder.job.telegram.comand.CommandManager;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TelegramPolling {
    private final CommandManager commandManager;

    @Value("${telegram.bot.token}")
    private String botToken;

    @PostConstruct
    private void init() {
        this.bot = new TelegramBot(botToken);
    }

    private long lastUpdateId = 0;
    private TelegramBot bot;

    @Scheduled(fixedDelayString = "${telegram.bot.polling.interval}")
    public void pollUpdates() {
        GetUpdatesResponse updatesResponse = bot.execute(new GetUpdates().limit(100).offset((int) lastUpdateId + 1));
        if (updatesResponse != null && updatesResponse.updates() != null) {
            for (Update update : updatesResponse.updates()) {
                lastUpdateId = update.updateId();

                Message message = update.message();
                if (message != null) {
                    commandManager.processCommand(message);
                }
            }
        }
    }

}
