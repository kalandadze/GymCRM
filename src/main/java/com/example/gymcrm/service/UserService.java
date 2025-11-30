package com.example.gymcrm.service;

import com.example.gymcrm.model.User;
import com.example.gymcrm.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@Slf4j
@Transactional
public class UserService {
  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User getUserByUsernameAndPassword(String username, String password) {
    return userRepository.findByUsernameAndPassword(username, password).orElseThrow(() -> new NoSuchElementException("username or password is incorrect"));
  }

  public void changePassword(String username, String oldPassword, String newPassword) {
    User user = getUserByUsernameAndPassword(username, oldPassword);
    user.setPassword(newPassword);
    userRepository.save(user);
  }
}
