package com.example.gymcrm.service;

import com.example.gymcrm.config.GymMetrics;
import com.example.gymcrm.model.Training;
import com.example.gymcrm.model.TrainingType;
import com.example.gymcrm.repository.TrainingRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@Transactional
public class TrainingService {
  private final TrainingRepository repository;
  private final GymMetrics metrics;

  @Autowired
  public TrainingService(TrainingRepository trainingRepository, GymMetrics metrics) {
    this.repository = trainingRepository;
    this.metrics = metrics;
  }

  public void save(Training training) {
    repository.save(training);
    metrics.incrementTrainingCreatedCounter();
  }

  public List<Training> getTrainings() {
    return repository.getAll();
  }

  public Training getTrainingById(Long id) {
    return repository.getTrainingById(id).orElseThrow(() -> new EntityNotFoundException("Training with id: " + id + " not found"));
  }

  public List<TrainingType> getAllTrainingTypes() {
    return repository.getAllTrainingTypes();
  }
}
