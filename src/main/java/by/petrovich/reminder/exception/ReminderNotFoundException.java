package by.petrovich.reminder.exception;

public class ReminderNotFoundException extends RuntimeException {
    public ReminderNotFoundException(Long id) {
        super(String.format("Reminder with id %d not found", id));
    }
}
