package com.example.gymcrm.repository;

import com.example.gymcrm.model.Training;
import com.example.gymcrm.model.TrainingType;

import java.util.List;
import java.util.Optional;


public interface TrainingRepository {
  void save(Training training);

  List<Training> getAll();

  Optional<Training> getTrainingById(Long id);

  List<TrainingType> getAllTrainingTypes();
}
