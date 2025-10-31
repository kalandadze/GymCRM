package com.example.gymcrm.repository;

import com.example.gymcrm.model.Trainer;

import java.util.List;


public interface TrainerRepository {
  void save(Trainer trainer);
  Trainer getTrainerByUsername(String username);
  List<Trainer> getAll();
  long countTrainersByUsernameLike(String username);
}
