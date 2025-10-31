package com.example.gymcrm.service;

import com.example.gymcrm.model.Trainee;
import com.example.gymcrm.repository.TraineeRepository;
import com.example.gymcrm.utils.PasswordGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TraineeService {
  private final TraineeRepository repository;

  @Autowired
  public TraineeService(TraineeRepository repository) {
    this.repository = repository;
  }

  public void createTrainee(Trainee trainee) {
    String username = trainee.getFirstName() + "." + trainee.getLastName();
    long count;
    if ((count = repository.countTraineesByUsernameLike(username)) != 0) username += count;

    trainee.setUsername(username);
    trainee.setPassword(PasswordGenerator.generatePassword(10));
    repository.save(trainee);
  }

  public void deleteTrainee(Trainee trainee) {
    repository.delete(trainee);
  }

  public void updateTrainee(Trainee trainee) {
    repository.save(trainee);
  }

  public Trainee getTrainee(String username) {
    return repository.getTraineeByUsername(username);
  }
}
