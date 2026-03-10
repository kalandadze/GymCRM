package com.example.gymcrm.repository;

import com.example.gymcrm.model.User;

import java.util.Optional;

public interface UserRepository {
  Optional<User> findByUsernameAndPassword(String username, String password);

  void save(User user);
}
