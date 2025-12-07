package com.example.gymcrm.service;

import com.example.gymcrm.model.GymUser;
import com.example.gymcrm.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Slf4j
@Transactional
public class UserService {
  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
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
}
