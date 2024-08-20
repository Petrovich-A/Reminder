package by.petrovich.reminder.service.impl;

import by.petrovich.reminder.exception.ReminderNotFoundException;
import by.petrovich.reminder.model.User;
import by.petrovich.reminder.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl {
    private final UserRepository userRepository;

    public void partialUpdate(long userId, long userTelegramId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new ReminderNotFoundException("User not found");
        } else {
            optionalUser.get().setChatId(String.valueOf(userTelegramId));
            userRepository.save(optionalUser.get());
        }
    }
}
