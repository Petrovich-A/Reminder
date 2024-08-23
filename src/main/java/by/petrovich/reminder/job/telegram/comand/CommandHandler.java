package by.petrovich.reminder.job.telegram.comand;

import org.telegram.telegrambots.meta.api.objects.Message;

public interface CommandHandler {
    void handle(Message message);

    String getCommand();
}
