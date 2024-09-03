package by.petrovich.reminder.controller;

import by.petrovich.reminder.dto.request.RegistrationRequestDto;
import by.petrovich.reminder.dto.response.RegistrationResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/registration")
public class RegistrationController {
    private

    @GetMapping("/registration")
    public ResponseEntity<RegistrationResponseDto> registration(@RequestBody @Valid RegistrationRequestDto registrationRequestDto) {


    }
}
