package com.example.gymcrm.repository.implementation;

import com.example.gymcrm.database.TraineeDatabase;
import com.example.gymcrm.database.TrainerDatabase;
import com.example.gymcrm.model.Trainee;
import com.example.gymcrm.model.Training;
import com.example.gymcrm.repository.TraineeRepository;
import com.example.gymcrm.repository.TrainingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TraineeRepositoryImplementation implements TraineeRepository {
  private final TraineeDatabase database;

  public TraineeRepositoryImplementation(TraineeDatabase database) {
    this.database = database;
  }

  @Override
  public void save(Trainee trainee) {
    database.addTrainee(trainee);
  }

  @Override
  public void delete(Trainee trainee) {
    database.deleteTrainee(trainee);
  }

  @Override
  public Trainee getTraineeByUsername(String Username) {
    return database.getTrainee(Username);
  }

  @Override
  public long countTraineesByUsernameLike(String username) {
    return database.getAllTrainees().filter(trainee -> trainee.getUsername().startsWith(username)).count();
  }

  @Override
  public List<Trainee> getAll() {
    return database.getAllTrainees().toList();
  }
}
