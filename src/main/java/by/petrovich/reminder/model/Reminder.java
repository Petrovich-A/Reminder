package by.petrovich.reminder.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Schema(description = "Entity representing a reminder")
public class Reminder {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reminder_seq")
    @SequenceGenerator(name = "reminder_seq", sequenceName = "reminder_id_seq", allocationSize = 10)
    @Column(columnDefinition = "bigint")
    @Schema(description = "Unique identifier of the reminder", example = "1")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "Title of the reminder", example = "Meeting")
    private String title;

    @Column(length = 4096, nullable = false)
    @Schema(description = "Description of the reminder", example = "Team meeting at 10 AM")
    private String description;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime remind;

    @Column(nullable = false)
    @Schema(description = "ID of the user who created the reminder", example = "3")
    private Long userId;

}

