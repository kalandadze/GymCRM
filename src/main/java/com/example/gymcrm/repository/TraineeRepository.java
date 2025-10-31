package com.example.gymcrm.repository;

import com.example.gymcrm.model.Trainee;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface TraineeRepository {
  void save(Trainee trainee);
  void delete(Trainee trainee);
  Trainee getTraineeByUsername(String Username);
  long countTraineesByUsernameLike(String username);
  List<Trainee> getAll();
}
