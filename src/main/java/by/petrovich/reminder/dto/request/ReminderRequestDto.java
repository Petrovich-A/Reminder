package by.petrovich.reminder.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static by.petrovich.reminder.constant.Constant.FORMAT_PATTERN;

@Data
@Builder
public class ReminderRequestDto {
    @NotBlank(message = "Title must not be blank")
    @Size(max = 255)
    private String title;

    @NotBlank(message = "Description must not be blank")
    @Size(max = 4096)
    private String description;

    @Future
    @DateTimeFormat(pattern = FORMAT_PATTERN)
    private LocalDateTime remind;

    @NotNull
    private Long userId;

}
