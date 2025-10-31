package com.example.gymcrm.service;

import com.example.gymcrm.database.TrainingDatabase;
import com.example.gymcrm.model.Trainee;
import com.example.gymcrm.model.Training;
import com.example.gymcrm.repository.TraineeRepository;
import com.example.gymcrm.repository.TrainingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TrainingService {
  private final TrainingRepository repository;

  @Autowired
  public TrainingService(TrainingRepository trainingRepository) {
    this.repository = trainingRepository;
  }

  public void createTraining(Training training) {
    repository.save(training);
  }

  public List<Training> getTrainings(){
    return repository.getAll();
  }
}
