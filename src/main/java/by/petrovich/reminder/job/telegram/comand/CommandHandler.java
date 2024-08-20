package by.petrovich.reminder.job.telegram.comand;

import com.pengrad.telegrambot.model.Message;

public interface CommandHandler {
    void handle(Message message);

    String getCommand();
}
