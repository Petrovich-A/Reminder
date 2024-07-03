package by.petrovich.reminder.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "DTO for reminder response")
public class ReminderResponseDto {
    @Schema(description = "Unique identifier of the reminder", example = "1")
    private Long id;

    @Schema(description = "Title of the reminder", example = "Meeting")
    private String title;

    @Schema(description = "Description of the reminder", example = "Team meeting at 10 AM")
    private String description;

    @Schema(description = "ID of the user who created the reminder", example = "3")
    private Long userId;
}
