package by.petrovich.reminder.model;

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
@RequiredArgsConstructor
@Table(name = "reminders")
public class Reminder {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reminder_seq")
    @SequenceGenerator(name = "reminder_seq", sequenceName = "reminder_id_seq", allocationSize = 10)
    @NotNull
    @Column(nullable = false, columnDefinition = "bigint")
    private Long id;

    @Column(nullable = false)
    @NotNull
    @Size(max = 255)
    private String title;

    @Column(length = 4096, nullable = false)
    @NotNull
    @Size(max = 4096)
    private String description;

    @Column(nullable = false)
    private LocalDateTime remind;

    @Column(name = "user_id", nullable = false)
    @NotNull
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

}

