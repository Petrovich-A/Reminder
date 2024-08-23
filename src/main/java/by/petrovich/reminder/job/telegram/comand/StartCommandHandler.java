package by.petrovich.reminder.job.telegram.comand;

import by.petrovich.reminder.sender.impl.TelegramSender;
import by.petrovich.reminder.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@RequiredArgsConstructor
public class StartCommandHandler implements CommandHandler {
    private static final Logger logger = LoggerFactory.getLogger(TelegramSender.class);
    private final UserServiceImpl userService;

    @Override
    public String getCommand() {
        return "/start";
    }

    @Override
    public void handle(Message message) {
        String[] parts = message.getText().split(" ");
        if (parts.length > 1) {
            try {
                Long userTelegramId = message.getChatId();
                Long userId = Long.valueOf(parts[1]);
                logger.info("Received data from Telegram: userId: {}, userTelegramId: {}", userId, userTelegramId);
                userService.partialUpdate(userId, userTelegramId);
            } catch (NumberFormatException e) {
                logger.error("Invalid userId format: {}", parts[1]);
            }
        }
    }
}
