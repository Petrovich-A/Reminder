package by.petrovich.reminder.job.telegram.comand;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Map;

@Component
@Log4j2
@RequiredArgsConstructor
public class CommandManager {
    private final Map<String, CommandHandler> commandHandlers;

    public void processCommand(Message message) {
        String text = message.getText();
        if (text != null) {
            String command = text.split(" ")[0];
            CommandHandler handler = commandHandlers.get(command);
            if (handler != null) {
                handler.handle(message);
            } else {
                log.error("Command not found");
            }
        }
    }
}
