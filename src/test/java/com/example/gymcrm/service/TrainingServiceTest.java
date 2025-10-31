package com.example.gymcrm.service;

import com.example.gymcrm.model.Trainee;
import com.example.gymcrm.model.Training;
import com.example.gymcrm.model.TrainingType;
import com.example.gymcrm.repository.TraineeRepository;
import com.example.gymcrm.repository.TrainingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TrainingServiceTest {
  private TrainingService trainingService;
  private TrainingRepository trainingRepository;

  @BeforeEach
  void setUpMock() {
    this.trainingRepository = Mockito.mock(TrainingRepository.class);
    this.trainingService = new TrainingService(trainingRepository);
  }

  @Test
  void createTrainer() {
    Training training = new Training("trainingName","traineeId","trainerId",new TrainingType("trainingType"), LocalDateTime.now(), Duration.ofHours(1));
    trainingService.createTraining(training);
    Mockito.verify(trainingRepository).save(training);
  }

  @Test
  void getTrainer() {
    List<Training> trainings = new ArrayList<>();
    for (int i = 1; i <= 5; i++) {
      Training training = new Training("trainingName"+i,"traineeId"+i,"trainerId"+i,new TrainingType("trainingType"+i), LocalDateTime.now(), Duration.ofHours(1));
      trainings.add(training);
    }
    Mockito.when(trainingRepository.getAll()).thenReturn(trainings);
    assertEquals(trainings, trainingService.getTrainings());
  }
}