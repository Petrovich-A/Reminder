package by.petrovich.reminder.client;

import by.petrovich.reminder.model.Reminder;
import by.petrovich.reminder.model.User;

public interface Sender {
    void sendMessage(Reminder reminder);
}
