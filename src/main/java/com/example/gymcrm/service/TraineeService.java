package com.example.gymcrm.service;

import com.example.gymcrm.model.Trainee;
import com.example.gymcrm.model.Trainer;
import com.example.gymcrm.model.Training;
import com.example.gymcrm.model.TrainingType;
import com.example.gymcrm.repository.TraineeRepository;
import com.example.gymcrm.utils.PasswordGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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

  public void deleteTrainee(String username, String password) {
    repository.delete(username, password);
  }

  public void updateTrainee(Trainee trainee, String username, String password) {
    repository.save(trainee);
  }

  public Trainee getTrainee(String username, String password) {
    return repository.getTraineeByUsername(username, password);
  }

  public void changePassword(String username, String oldPassword, String newPassword) {
    Trainee trainee = repository.getTraineeByUsername(username, oldPassword);
    trainee.setPassword(newPassword);
    repository.update(trainee, trainee.getUsername(), oldPassword);
  }

  public void changeActivity(String username, String password, boolean activity) {
    Trainee trainee = repository.getTraineeByUsername(username, password);
    trainee.setActive(activity);
    repository.update(trainee, trainee.getUsername(), password);
  }

  public List<Training> getTrainingsByUsernameAndCriteria(String username, String password, LocalDateTime startDate, LocalDateTime endDate, String trainerName, TrainingType trainingType){
    return repository.getTrainingsByUsernameAndCriteria(username, password, startDate, endDate, trainerName, trainingType);
  }

  public List<Trainer> getTrainersByTraineeUsername(String username, String password) {
    Trainee trainee = repository.getTraineeByUsername(username, password);
    return trainee.getTrainers();
  }

  public void addTrainer(String username, String password, Trainer trainer) {
    Trainee trainee = repository.getTraineeByUsername(username, password);
    trainee.getTrainers().add(trainer);
    repository.update(trainee, trainee.getUsername(), password);
  }

  public void removeTrainer(String username, String password, String trainerUsername) {
    Trainee trainee = repository.getTraineeByUsername(username, password);
    List<Trainer> trainers = trainee.getTrainers();
    Trainer trainer=trainers.stream().filter(it->it.getUsername().equals(trainerUsername)).findFirst().orElseThrow();
    trainers.remove(trainer);
    repository.update(trainee, trainee.getUsername(), password);
  }
}
