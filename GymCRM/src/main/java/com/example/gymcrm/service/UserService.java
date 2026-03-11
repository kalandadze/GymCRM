package com.example.gymcrm.service;

import com.example.gymcrm.model.GymUser;
import com.example.gymcrm.repository.UserRepository;
import com.example.gymcrm.utils.JwtUtils;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Slf4j
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private static final int MAX_FAILED_ATTEMPTS = 3;
    private static final int BLOCK_MINUTES = 5;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    public GymUser getUserByUsernameAndPassword(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password).orElseThrow(() -> new NoSuchElementException("username or password is incorrect"));
    }

    public Optional<GymUser> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void changePassword(String username, String newPassword) {
        GymUser user = getUserByUsername(username).orElseThrow(() -> new NoSuchElementException("username is incorrect"));
        user.setPassword(newPassword);
        userRepository.updatePassword(user);
    }

    public String login(String username, String password) {
        GymUser user = getUserByUsernameAndPassword(username, password);
        if (user.getBlockedUntil() != null && user.getBlockedUntil().isAfter(LocalDateTime.now()))
            throw new RuntimeException("User blocked until " + user.getBlockedUntil());
        if (user.getLoginTries() == null) user.setLoginTries(0);
        if (!passwordEncoder.matches(password, user.getPassword())) {
            user.setLoginTries(user.getLoginTries() + 1);
            if (user.getLoginTries() >= MAX_FAILED_ATTEMPTS) {
                user.setBlockedUntil(LocalDateTime.now().plusMinutes(BLOCK_MINUTES));
                user.setLoginTries(0);
                user.setBlockedUntil(null);
            }
            userRepository.save(user);
            throw new NoSuchElementException("username or password is incorrect");
        }
        user.setLoginTries(0);
        userRepository.save(user);
        return jwtUtils.generateToken(user.getUsername());
    }
}
