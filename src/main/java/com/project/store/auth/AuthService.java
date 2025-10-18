package com.project.store.auth;

import com.project.store.config.JwtService;
import com.project.store.exception.UserAlreadyRegisteredException;
import com.project.store.models.user.Role;
import com.project.store.models.user.User;
import com.project.store.repository.UserRepository;
import com.project.store.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CartService cartService;

    public String register(RegisterRequest request) {
        if (userService.checkUserExists(request.getEmail())) {
            throw new UserAlreadyRegisteredException("User with email " + request.getEmail() + " is already registered.");
        }
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        userRepository.save(user);

        return jwtService.generateToken(user);
    }

    public String login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword())
        );
        cartService.clearCart(request.getEmail());
        User user = userService.getUserByEmail(request.getEmail());
        return jwtService.generateToken(user);
    }
}
