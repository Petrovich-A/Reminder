package by.petrovich.reminder.job.telegram;

import by.petrovich.reminder.job.telegram.comand.CommandManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class TelegramReceiver extends TelegramLongPollingBot {
    private final CommandManager commandManager;

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.bot.name}")
    private String botName;


    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message incomeMessage = update.getMessage();
            if (incomeMessage.hasText()) {
                commandManager.processCommand(incomeMessage);
            }
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}
