package by.petrovich.reminder.sender;

import by.petrovich.reminder.sender.message.MessageToSend;

public interface Sender<E, M extends MessageToSend> {
    void sendMessage(M messageToSend);

    M createMessage(E entity);
}
