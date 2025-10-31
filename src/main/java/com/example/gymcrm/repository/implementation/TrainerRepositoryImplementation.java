package com.example.gymcrm.repository.implementation;

import com.example.gymcrm.database.TrainerDatabase;
import com.example.gymcrm.model.Trainer;
import com.example.gymcrm.model.Training;
import com.example.gymcrm.repository.TrainerRepository;
import com.example.gymcrm.repository.TrainingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TrainerRepositoryImplementation implements TrainerRepository {
  private final TrainerDatabase database;

  @Autowired
  public TrainerRepositoryImplementation(TrainerDatabase database) {
    this.database = database;
  }

  @Override
  public void save(Trainer trainer) {
    database.addTrainer(trainer);
  }

  @Override
  public Trainer getTrainerByUsername(String username) {
    return database.getTrainer(username);
  }

  @Override
  public List<Trainer> getAll() {
    return database.getAllTrainers().toList();
  }

  @Override
  public long countTrainersByUsernameLike(String username) {
    return database.getAllTrainers().filter(trainer -> trainer.getUsername().startsWith(username)).count();
  }
}
