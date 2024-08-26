package by.petrovich.reminder.job.telegram.comand;

import by.petrovich.reminder.sender.impl.TelegramSender;
import by.petrovich.reminder.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import static by.petrovich.reminder.utils.EncodingUtils.decodeBase64;

@Component("/start")
@RequiredArgsConstructor
@Log4j2
public class StartCommandHandler implements CommandHandler {
    private final UserServiceImpl userService;

    @Override
    public void handle(Message message) {
        String[] parts = message.getText().split(" ");
        if (parts.length > 1) {
            try {
                Long userTelegramId = message.getChatId();
                String userIdEncode = parts[1];
                Long decodeUserId = Long.parseLong(decodeBase64(userIdEncode));
                log.info("Received data from Telegram: userId: {}, userTelegramId: {}", decodeUserId, userTelegramId);
                userService.partialUpdate(decodeUserId, userTelegramId);
            } catch (NumberFormatException e) {
                log.error("Invalid userId format: {}", parts[1]);
            }
        }
    }
}
