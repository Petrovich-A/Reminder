package by.petrovich.reminder.sender.message;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class RegistrationMessageToSend extends MessageToSend {
    private String attachment;

}
