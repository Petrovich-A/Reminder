package by.petrovich.reminder.job.telegram.comand;

import by.petrovich.reminder.job.telegram.TelegramPolling;
import by.petrovich.reminder.service.impl.UserServiceImpl;
import com.pengrad.telegrambot.model.Message;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StartCommandHandler implements CommandHandler {
    private static final Logger logger = LoggerFactory.getLogger(TelegramPolling.class);
    private final UserServiceImpl userService;

    @Override
    public String getCommand() {
        return "/start";
    }

    @Override
    public void handle(Message message) {
        String[] parts = message.text().split(" ");
        if (parts.length > 1) {
            try {
                Long userTelegramId = message.from().id();
                long userId = Long.parseLong(parts[1]);
                logger.info("Received data from Telegram: userId: {}, userTelegramId: {}", userId, userTelegramId);
                userService.partialUpdate(userId, userTelegramId);
            } catch (NumberFormatException e) {
                logger.error("Invalid userId format: {}", parts[1]);
            }
        }
    }
}
