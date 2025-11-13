package com.example.gymcrm.service;

import com.example.gymcrm.model.Trainee;
import com.example.gymcrm.model.Trainer;
import com.example.gymcrm.model.Training;
import com.example.gymcrm.repository.TrainerRepository;
import com.example.gymcrm.utils.PasswordGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class TrainerService {
  private final TrainerRepository repository;

  @Autowired
  public TrainerService(TrainerRepository trainerRepository) {
    this.repository = trainerRepository;
  }

  public void createTrainer(Trainer trainer) {
    String username = trainer.getFirstName() + "." + trainer.getLastName();
    System.out.println(username);
    long count;
    if ((count = repository.countTrainersByUsernameLike(username)) != 0) username += count;

    trainer.setUsername(username);
    trainer.setPassword(PasswordGenerator.generatePassword(10));
    repository.save(trainer);
  }

  public void updateTrainer(Trainer trainer, String username, String password) {
    repository.update(trainer, username, password);
  }

  public void updateActivity(String username, String password, boolean isActive) {
    Trainer trainer = repository.getTrainerByUsername(username, password);
    trainer.setActive(isActive);
    repository.update(trainer, username, password);
  }

  public Trainer getTrainer(String username, String password) {
    return repository.getTrainerByUsername(username, password);
  }

  public void changePassword(String username, String oldPassword, String newPassword) {
    Trainer trainer = repository.getTrainerByUsername(username, oldPassword);
    trainer.setPassword(newPassword);
    repository.update(trainer, trainer.getUsername(), oldPassword);
  }

  List<Training> getTrainingsByUsernameAndCriteria(String username, String password, LocalDateTime startDate, LocalDateTime endDate, String traineeName){
    return repository.getTrainingsByUsernameAndCriteria(username, password, startDate, endDate, traineeName);
  };

}
