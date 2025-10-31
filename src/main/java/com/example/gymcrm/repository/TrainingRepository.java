package com.example.gymcrm.repository;

import com.example.gymcrm.model.Training;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface TrainingRepository {
  void save(Training training);
  List<Training> getAll();
}
