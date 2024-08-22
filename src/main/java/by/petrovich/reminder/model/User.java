package by.petrovich.reminder.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "user_id_seq", allocationSize = 10)
    @Column(nullable = false, columnDefinition = "bigint")
    @NotNull
    private Long id;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String name;

    @Column(nullable = false)
    @NotNull
    @Size(min = 8, max = 100)
    private String password;

    @Column(nullable = false)
    @NotNull
    @Email
    @Size(max = 50)
    private String email;

    @Column(name = "telegram_user_id", nullable = false)
    @NotNull
    @Min(1)
    private Long telegramUserId;

    @Column(name = "o_auth_provider", nullable = false)
    @NotNull
    @Size(min = 3, max = 15)
    private String oAuthProvider;

    @OneToMany(mappedBy = "user")
    private Set<Reminder> reminders;

}
