package by.petrovich.reminder.sender.message;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MessageToSend {
    private String toRecipient;
    private String subject;
    private String body;
}
