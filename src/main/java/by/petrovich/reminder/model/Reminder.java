package by.petrovich.reminder.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Schema(description = "Entity representing a reminder")
@RequiredArgsConstructor
@Table(name = "reminders")
public class Reminder {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reminder_seq")
    @SequenceGenerator(name = "reminder_seq", sequenceName = "reminder_id_seq", allocationSize = 10)
    @Schema(description = "Unique identifier of the reminder", example = "1")
    @NotNull
    @Column(nullable = false, columnDefinition = "bigint")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "Title of the reminder", example = "Meeting")
    @NotNull
    @Size(max = 255)
    private String title;

    @Column(length = 4096, nullable = false)
    @Schema(description = "Description of the reminder", example = "Team meeting at 10 AM")
    @NotNull
    @Size(max = 4096)
    private String description;

    @Column(nullable = false)
    @Schema(description = "Date and time of the reminder", example = "2024-12-03 16:00:00")
    private LocalDateTime remind;

    @Column(name = "user_id", nullable = false)
    @Schema(description = "ID of the user who created the reminder", example = "3")
    @NotNull
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

}

