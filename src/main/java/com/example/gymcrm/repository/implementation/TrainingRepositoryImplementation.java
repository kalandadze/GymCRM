package com.example.gymcrm.repository.implementation;

import com.example.gymcrm.database.TrainingDatabase;
import com.example.gymcrm.model.Training;
import com.example.gymcrm.repository.TrainingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TrainingRepositoryImplementation implements TrainingRepository {
  private final TrainingDatabase database;

  @Autowired
  public TrainingRepositoryImplementation(TrainingDatabase database) {
    this.database = database;
  }

  @Override
  public void save(Training training) {
    database.addTraining(training);
  }

  @Override
  public List<Training> getAll() {
    return database.getAllTrainings();
  }
}
