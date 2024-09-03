package by.petrovich.reminder.service.impl;

import by.petrovich.reminder.dto.request.RegistrationRequestDto;
import by.petrovich.reminder.dto.response.RegistrationResponseDto;
import by.petrovich.reminder.exception.UserNotFoundException;
import by.petrovich.reminder.model.User;
import by.petrovich.reminder.repository.UserRepository;
import by.petrovich.reminder.utils.EncodingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl {
    private final UserRepository userRepository;

    @Transactional
    public RegistrationResponseDto registration(RegistrationRequestDto registrationRequestDto) {
        if (!registrationRequestDto.getPassword().equals(registrationRequestDto.getMatchingPassword())) {
            throw new InvalidPasswordException("Пароли не совпадают");

        }
        Optional<User> existingUser = userRepository.findByEmail(registrationRequestDto.getEmail());
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException("Пользователь с таким email уже существует");
        }
        String encodePassword = EncodingUtils.encodeBase64(registrationRequestDto.getPassword());
        RegistrationResponseDto registrationResponseDto = new RegistrationResponseDto();

        User user = new User();
        user.setName(registrationRequestDto.getName());
        user.setPassword(encodePassword);
        user.setEmail(registrationRequestDto.getEmail());
        User savedUser = userRepository.save(user);

        registrationResponseDto.setId(savedUser.getId());
        registrationResponseDto.setName(savedUser.getName());
        return registrationResponseDto;
    }

    @Transactional
    public void partialUpdate(Long userId, Long userTelegramId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        user.setTelegramUserId(userTelegramId);
        userRepository.save(user);
    }
}
