package by.petrovich.reminder.service.impl;

import by.petrovich.reminder.model.User;
import by.petrovich.reminder.repository.UserRepository;
import by.petrovich.reminder.sender.Sender;
import by.petrovich.reminder.sender.message.MessageToSend;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
    private final Sender<User> sendRegistrationMessage;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String oAuthProvider = userRequest.getClientRegistration().getRegistrationId();

        User user = userRepository.findByEmail(email).orElse(null);

        if (user != null){
            user.setName(name);
            user.setOAuthProvider(oAuthProvider);
            userRepository.save(user);
        }else {
            user = createNewUser(name, email, oAuthProvider);
            User savedUser = userRepository.save(user);
            MessageToSend messageToSend = sendRegistrationMessage.createMessage(savedUser);
            sendRegistrationMessage.sendMessage(messageToSend);
        }
        return oAuth2User;
    }

    private User createNewUser(String name, String email, String oAuthProvider) {
        // TODO: 18.08.2024 plug
        User user = new User();
        user.setName(name);
        user.setPassword("defaultPassword");
        user.setEmail(email);
        user.setTelegramUserId(0L);
        user.setOAuthProvider(oAuthProvider);
        return user;
    }

}
