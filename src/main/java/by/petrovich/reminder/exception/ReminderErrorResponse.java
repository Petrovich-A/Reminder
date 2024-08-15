package by.petrovich.reminder.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReminderErrorResponse {
    private int status;
    private String error;
    private String message;
}
