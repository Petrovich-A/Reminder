package by.petrovich.reminder.service.impl;

import by.petrovich.reminder.exception.UserNotFoundException;
import by.petrovich.reminder.model.User;
import by.petrovich.reminder.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl {
    private final UserRepository userRepository;

    @Transactional
    public void partialUpdate(Long userId, Long userTelegramId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        user.setTelegramUserId(userTelegramId);
        userRepository.save(user);
    }
}
