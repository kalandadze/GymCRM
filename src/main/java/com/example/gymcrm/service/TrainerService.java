package com.example.gymcrm.service;

import com.example.gymcrm.model.Trainee;
import com.example.gymcrm.model.Trainer;
import com.example.gymcrm.repository.TrainerRepository;
import com.example.gymcrm.utils.PasswordGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

  public void updateTrainer(Trainer trainer) {
    repository.save(trainer);
  }

  public Trainer getTrainer(String username) {
    return repository.getTrainerByUsername(username);
  }

}
