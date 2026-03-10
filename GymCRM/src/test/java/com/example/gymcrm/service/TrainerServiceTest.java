package com.example.gymcrm.service;

import com.example.gymcrm.model.Trainer;
import com.example.gymcrm.model.TrainingType;
import com.example.gymcrm.repository.TrainerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TrainerServiceTest {
  private TrainerService trainerService;
  private TrainerRepository trainerRepository;

  @BeforeEach
  void setUpMock() {
    this.trainerRepository = Mockito.mock(TrainerRepository.class);
    this.trainerService = new TrainerService(trainerRepository);
  }

  @Test
  void createTrainer() {
    Trainer trainer = new Trainer("name", "lname", "specilization", "userID", new TrainingType("training Type"));
    trainerService.save(trainer);
    Mockito.verify(trainerRepository).save(trainer);
  }

  @Test
  void getTrainer() {
    Trainer trainer = new Trainer("name", "lname", "specilization", "userID", new TrainingType("training Type"));
    Mockito.when(trainerRepository.getTrainerByUsername("name.lname")).thenReturn(trainer);
    assertEquals(trainer, trainerService.getTrainer("name.lname"));
  }

  @Test
  void updateTrainerActivity() {
    Trainer trainer = new Trainer("name", "lname", "specilization", "userID", new TrainingType("training Type"));
    Mockito.when(trainerRepository.getTrainerByUsername("name.lname")).thenReturn(trainer);
    trainerService.updateActivity("name.lname", true);
    assertTrue(trainer.isActive());

    trainer.setActive(true);
    Mockito.verify(trainerRepository).update(trainer, "name.lname");

    trainerService.updateActivity("name.lname", false);
    assertFalse(trainer.isActive());
  }


  @Test
  void updateTrainerPassword() {
    Trainer trainer = new Trainer("name", "lname", "specilization", "userID", new TrainingType("training Type"));
    trainer.setUsername("name.lname");
    Mockito.when(trainerRepository.getTrainerByUsername("name.lname")).thenReturn(trainer);
    trainerService.changePassword("name.lname", "newPassword");
    assertEquals("newPassword", trainer.getPassword());

    Mockito.verify(trainerRepository).update(trainer, "name.lname");
  }


}