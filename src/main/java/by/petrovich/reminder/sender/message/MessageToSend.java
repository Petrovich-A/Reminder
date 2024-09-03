package by.petrovich.reminder.sender.message;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class MessageToSend {
    private String toRecipient;
    private String subject;
    private String body;
}
