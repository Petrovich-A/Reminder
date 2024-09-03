package by.petrovich.reminder.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegistrationRequestDto {
    @NotNull
    @Size(max = 50)
    private String name;

    @NotNull
    @Email
    @Size(max = 50)
    private String email;

    @NotNull
    @Size(min = 8, max = 100)
    private String password;

    @NotNull
    @Size(min = 8, max = 100)
    private String matchingPassword;

}
