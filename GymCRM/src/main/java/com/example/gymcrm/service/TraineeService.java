package com.example.gymcrm.service;

import com.example.gymcrm.config.GymMetrics;
import com.example.gymcrm.model.LoginInfo;
import com.example.gymcrm.model.Trainee;
import com.example.gymcrm.model.Trainer;
import com.example.gymcrm.model.Training;
import com.example.gymcrm.repository.TraineeRepository;
import com.example.gymcrm.utils.PasswordGenerator;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
@Transactional
public class TraineeService {
  private final TraineeRepository repository;
  private final GymMetrics metrics;

  @Autowired
  public TraineeService(TraineeRepository repository, GymMetrics metrics) {
    this.repository = repository;
    this.metrics = metrics;
  }

  public LoginInfo save(Trainee trainee) {
    String username = trainee.getFirstName() + "." + trainee.getLastName();
    long count;
    if ((count = repository.countTraineesByUsernameLike(username)) != 0) username += count;

    String password = PasswordGenerator.generatePassword(10);
    trainee.setUsername(username);
    trainee.setPassword(password);
    repository.save(trainee);
    metrics.incrementTraineeCreatedCounter();
    return new LoginInfo(trainee.getUsername(), password);
  }

  public void deleteTrainee(String username) {
    getTrainee(username);
    repository.delete(username);
  }

  public void updateTrainee(Trainee trainee, String username) {
    repository.update(trainee, username);
  }

  public Trainee getTrainee(String username) {
    return repository.getTraineeByUsername(username).orElseThrow(() -> new NoSuchElementException("username or password is incorrect"));
  }

  public void changePassword(String username, String newPassword) {
    Trainee trainee = getTrainee(username);
    trainee.setPassword(newPassword);
    repository.update(trainee, trainee.getUsername());
  }

  public void changeActivity(String username, boolean activity) {
    Trainee trainee = getTrainee(username);
    trainee.setActive(activity);
    repository.update(trainee, trainee.getUsername());
  }

  public List<Training> getTrainingsByUsernameAndCriteria(String username, LocalDateTime startDate, LocalDateTime endDate, String trainerName, String trainingType) {
    return repository.getTrainingsByUsernameAndCriteria(username, startDate, endDate, trainerName, trainingType);
  }

  public List<Trainer> getTrainersByTraineeUsername(String username) {
    Trainee trainee = getTrainee(username);
    return trainee.getTrainers();
  }

  public void addTrainer(String username, Trainer trainer) {
    Trainee trainee = getTrainee(username);
    trainee.getTrainers().add(trainer);
    repository.update(trainee, trainee.getUsername());
  }

  public void removeTrainer(String username, String trainerUsername) {
    Trainee trainee = getTrainee(username);
    List<Trainer> trainers = trainee.getTrainers();
    Trainer trainer = trainers.stream().filter(it -> it.getUsername().equals(trainerUsername)).findFirst().orElseThrow();
    trainers.remove(trainer);
    repository.update(trainee, trainee.getUsername());
  }

  public Trainee getTraineeByUsername(String traineeUsername) {
    return repository.getTraineeByUsername(traineeUsername).orElseThrow(() -> new NoSuchElementException("no trainee with username: " + traineeUsername + " found"));
  }
}
