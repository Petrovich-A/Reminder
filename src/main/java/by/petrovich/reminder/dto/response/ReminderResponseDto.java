package by.petrovich.reminder.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ReminderResponseDto {
    private Long id;

    private String title;

    private String description;

    private LocalDateTime remind;

    private Long userId;
}
