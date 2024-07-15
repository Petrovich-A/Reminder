package by.petrovich.reminder.repository;

import by.petrovich.reminder.model.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long> {
    List<Reminder> findByTitleContainingIgnoreCase(String title);

    List<Reminder> findByDescriptionContainingIgnoreCase(String description);

    List<Reminder> findRemindersByRemind(LocalDateTime date);

    List<Reminder> findRemindersByRemindBefore(LocalDateTime date);
}
