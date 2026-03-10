package com.example.gymcrm.repository;

import com.example.gymcrm.model.GymUser;

import java.util.Optional;

public interface UserRepository {
  Optional<GymUser> findByUsernameAndPassword(String username, String password);

  Optional<GymUser> findByUsername(String username);

  void save(GymUser user);

  void updatePassword(GymUser user);
}
