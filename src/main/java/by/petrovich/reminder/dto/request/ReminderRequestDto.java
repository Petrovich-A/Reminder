package by.petrovich.reminder.dto.request;

import lombok.Data;

@Data
public class ReminderRequestDto {
    private String title;

    private String description;

    private Long userId;
}
