package com.example.gymcrm.repository;

import com.example.gymcrm.model.Trainer;
import com.example.gymcrm.model.Training;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface TrainerRepository {
  void save(Trainer trainer);

  void update(Trainer trainer, String username);

  void delete(String username);

  Optional<Trainer> getTrainerByUsername(String Username);

  List<Trainer> getAll();

  long countTrainersByUsernameLike(String username);

  List<Training> getTrainingsByUsernameAndCriteria(String username, LocalDateTime startDate, LocalDateTime endDate, String traineeName);

}
