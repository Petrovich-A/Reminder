package by.petrovich.reminder.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO for creating a reminder")
public class ReminderRequestDto {
    @Schema(description = "Title of the reminder", example = "Meeting")
    private String title;

    @Schema(description = "Description of the reminder", example = "Team meeting at 10 AM")
    private String description;

    @Schema(description = "ID of the user who created the reminder", example = "3")
    private Long userId;

}
