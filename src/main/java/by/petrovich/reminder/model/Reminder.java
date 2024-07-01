package by.petrovich.reminder.model;

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
public class Reminder {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reminder_seq")
    @SequenceGenerator(name = "reminder_seq", sequenceName = "reminder_id_seq")
    @Column(columnDefinition = "bigint")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 4096, nullable = false)
    private String description;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime remind;

    private Long userId;

}

