package com.example.gymcrm.service;

import com.example.gymcrm.config.GymMetrics;
import com.example.gymcrm.model.LoginInfo;
import com.example.gymcrm.model.Trainer;
import com.example.gymcrm.model.Training;
import com.example.gymcrm.repository.TrainerRepository;
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
public class TrainerService {
  private final TrainerRepository repository;
  private final GymMetrics metrics;

  @Autowired
  public TrainerService(TrainerRepository trainerRepository, GymMetrics metrics) {
    this.repository = trainerRepository;
    this.metrics = metrics;
  }

  public LoginInfo save(Trainer trainer) {
    String username = trainer.getFirstName() + "." + trainer.getLastName();
    System.out.println(username);
    long count;
    if ((count = repository.countTrainersByUsernameLike(username)) != 0) username += count;

    String password = PasswordGenerator.generatePassword(10);
    trainer.setUsername(username);
    trainer.setPassword(password);
    repository.save(trainer);
    metrics.incrementTrainerCreatedCounter();
    return new LoginInfo(trainer.getUsername(), password);
  }

  public void updateTrainer(Trainer trainer, String username) {
    repository.update(trainer, username);
  }

  public void updateActivity(String username, boolean isActive) {
    Trainer trainer = getTrainer(username);
    trainer.setActive(isActive);
    repository.update(trainer, username);
  }

  public Trainer getTrainer(String username) {
    return repository.getTrainerByUsername(username).orElseThrow(() -> new NoSuchElementException("username or password is incorrect"));
  }

  public void changePassword(String username, String newPassword) {
    Trainer trainer = getTrainer(username);
    trainer.setPassword(newPassword);
    repository.update(trainer, trainer.getUsername());
  }

  public List<Training> getTrainingsByUsernameAndCriteria(String username, LocalDateTime startDate, LocalDateTime endDate, String traineeName) {
    return repository.getTrainingsByUsernameAndCriteria(username, startDate, endDate, traineeName);
  }

  public List<Trainer> getAllTrainers() {
    return repository.getAll();
  }

  public Trainer getTrainerByUsername(String username) {
    return repository.getTrainerByUsername(username).orElseThrow(() -> new NoSuchElementException("trainer with username: " + username + " not found"));
  }
}
