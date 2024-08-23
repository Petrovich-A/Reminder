package by.petrovich.reminder.sender;

import by.petrovich.reminder.sender.message.MessageToSend;

public interface Sender<T> {
    void sendMessage(MessageToSend messageToSend);

    MessageToSend createMessage(T entity);
}
