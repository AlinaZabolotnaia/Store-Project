package com.project.store.service;

import com.project.store.exception.UserNotFoundException;
import com.project.store.models.user.User;
import com.project.store.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("User with email [%s] not found", email)));
    }

    public boolean checkUserExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
