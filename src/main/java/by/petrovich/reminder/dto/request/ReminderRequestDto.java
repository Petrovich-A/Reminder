package by.petrovich.reminder.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static by.petrovich.reminder.constant.Constant.FORMAT_PATTERN;

@Data
@Schema(description = "DTO for creating a reminder")
public class ReminderRequestDto {
    @Schema(description = "Title of the reminder", example = "Meeting")
    @NotBlank(message = "Title must not be blank")
    @Size(max = 255)
    private String title;

    @Schema(description = "Description of the reminder", example = "Team meeting at 10 AM")
    @NotBlank(message = "Description must not be blank")
    @Size(max = 4096)
    private String description;

    @Schema(description = "Date and time of the reminder", example = "2024-12-03T16:00:00")
    @Future
    @DateTimeFormat(pattern = FORMAT_PATTERN)
    private LocalDateTime remind;

    @Schema(description = "ID of the user who created the reminder", example = "3")
    @NotNull
    private Long userId;

}
