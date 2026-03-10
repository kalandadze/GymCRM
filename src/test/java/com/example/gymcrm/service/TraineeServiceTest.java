package com.example.gymcrm.service;

import com.example.gymcrm.model.Trainee;
import com.example.gymcrm.repository.TraineeRepository;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TraineeServiceTest {
  private TraineeService traineeService;
  private TraineeRepository traineeRepository;

  @BeforeEach
  void setUpMock() {
    this.traineeRepository = Mockito.mock(TraineeRepository.class);
    this.traineeService = new TraineeService(traineeRepository);
  }

  @Test
  void createTrainer() {
    Trainee trainee = new Trainee("name", "lname", LocalDate.of(2001,12,12),"address","userID");
    traineeService.createTrainee(trainee);
    Mockito.verify(traineeRepository).save(trainee);
  }

  @Test
  void getTrainer() {
    Trainee trainee = new Trainee("name", "lname", LocalDate.of(2001,12,12),"address","userID");
    Mockito.when(traineeRepository.getTraineeByUsername("name.lname","password")).thenReturn(trainee);
    assertEquals(trainee, traineeService.getTrainee("name.lname", "password"));
  }

  @Test
  void updateTraineeActivity() {
    Trainee trainee = new Trainee("name", "lname", LocalDate.of(2001,12,12),"address","userID");
    trainee.setUsername("name.lname");
    Mockito.when(traineeRepository.getTraineeByUsername("name.lname","password")).thenReturn(trainee);
    traineeService.changeActivity("name.lname","password",true);
    assertTrue(trainee.isActive());

    Mockito.verify(traineeRepository).update(trainee,"name.lname","password");

    traineeService.changeActivity("name.lname","password",false);
    assertFalse(trainee.isActive());
  }

  @Test
  void updateTraineePassword() {
    Trainee trainee = new Trainee("name", "lname", LocalDate.of(2001,12,12),"address","userID");
    trainee.setUsername("name.lname");
    Mockito.when(traineeRepository.getTraineeByUsername("name.lname","password")).thenReturn(trainee);

    traineeService.changePassword("name.lname","password","new password");
    assertEquals("new password", trainee.getPassword());

    Mockito.verify(traineeRepository).update(trainee,"name.lname","password");
  }
}