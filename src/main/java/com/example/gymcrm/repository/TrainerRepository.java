package com.example.gymcrm.repository;

import com.example.gymcrm.model.Trainer;
import com.example.gymcrm.model.Training;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface TrainerRepository {
  void save(Trainer trainer);

  void update(Trainer trainer, String username, String password);

  void delete(String username, String password);

  Optional<Trainer> getTrainerByUsername(String Username, String password);

  List<Trainer> getAll();

  long countTrainersByUsernameLike(String username);

  List<Training> getTrainingsByUsernameAndCriteria(String username, String password, LocalDateTime startDate, LocalDateTime endDate, String traineeName);

  Optional<Trainer> getTrainerByUsername(String username);
}
