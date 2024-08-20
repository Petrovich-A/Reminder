package by.petrovich.reminder.job.telegram.comand;

import by.petrovich.reminder.job.telegram.TelegramPolling;
import com.pengrad.telegrambot.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Component
public class CommandManager {
    private static final Logger logger = LoggerFactory.getLogger(TelegramPolling.class);
    private final Map<String, CommandHandler> commandHandlers = new HashMap<>();

    public CommandManager(List<CommandHandler> handlers) {
        for (CommandHandler handler : handlers) {
            commandHandlers.put(handler.getCommand(), handler);
        }
    }

    public void processCommand(Message message) {
        String text = message.text();
        if (text != null) {
            String command = text.split(" ")[0];
            CommandHandler handler = commandHandlers.get(command);
            if (handler != null) {
                handler.handle(message);
            } else {
                logger.error("Command not found");
            }
        }
    }
}
