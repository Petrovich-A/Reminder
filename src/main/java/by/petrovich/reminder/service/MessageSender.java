package by.petrovich.reminder.service;

import by.petrovich.reminder.model.Reminder;
import by.petrovich.reminder.model.User;

public interface MessageSender {
    void send(User user, Reminder reminder);
}
