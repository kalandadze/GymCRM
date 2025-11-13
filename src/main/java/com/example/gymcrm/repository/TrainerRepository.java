package com.example.gymcrm.repository;

import com.example.gymcrm.model.Trainee;
import com.example.gymcrm.model.Trainer;
import com.example.gymcrm.model.Training;
import com.example.gymcrm.model.TrainingType;

import java.time.LocalDateTime;
import java.util.List;


public interface TrainerRepository {
  void save(Trainer trainer);

  void update(Trainer trainer, String username, String password);

  void delete(String username, String password);

  Trainer getTrainerByUsername(String Username, String password);

  long countTrainersByUsernameLike(String username);

  List<Training> getTrainingsByUsernameAndCriteria(String username, String password, LocalDateTime startDate, LocalDateTime endDate, String traineeName);

}
