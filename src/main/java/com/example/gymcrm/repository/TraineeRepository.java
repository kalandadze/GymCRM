package com.example.gymcrm.repository;

import com.example.gymcrm.model.Trainee;
import com.example.gymcrm.model.Training;
import com.example.gymcrm.model.TrainingType;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


public interface TraineeRepository {
  void save(Trainee trainee);

  void update(Trainee trainee, String username, String password);

  void delete(String username, String password);

  Trainee getTraineeByUsername(String Username, String password);

  long countTraineesByUsernameLike(String username);

  List<Training> getTrainingsByUsernameAndCriteria(String username, String password, LocalDateTime startDate, LocalDateTime endDate, String trainerName, TrainingType trainingType);
}
