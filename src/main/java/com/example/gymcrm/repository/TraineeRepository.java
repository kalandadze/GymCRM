package com.example.gymcrm.repository;

import com.example.gymcrm.model.Trainee;
import com.example.gymcrm.model.Training;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface TraineeRepository {
  void save(Trainee trainee);

  void update(Trainee trainee, String username);

  void delete(String username);

  Optional<Trainee> getTraineeByUsername(String Username);

  long countTraineesByUsernameLike(String username);

  List<Training> getTrainingsByUsernameAndCriteria(String username, LocalDateTime startDate, LocalDateTime endDate, String trainerName, String trainingType);

  List<Training> getAll();

}
