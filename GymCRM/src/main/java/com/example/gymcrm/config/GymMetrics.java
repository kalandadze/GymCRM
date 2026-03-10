package com.example.gymcrm.config;


import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class GymMetrics {
  private final Counter traineeCreatedCounter;
  private final Counter trainerCreatedCounter;
  private final Counter trainingCreatedCounter;

  public GymMetrics(MeterRegistry meterRegistry) {
    this.traineeCreatedCounter = Counter.builder("gymcrm.trainee.created").description("number of trainees created").register(meterRegistry);
    this.trainerCreatedCounter = Counter.builder("gymcrm.trainer.created").description("number of trainers created").register(meterRegistry);
    this.trainingCreatedCounter = Counter.builder("gymcrm.training.created").description("number of trainings created").register(meterRegistry);
  }

  public void incrementTraineeCreatedCounter() {
    traineeCreatedCounter.increment();
  }

  public void incrementTrainerCreatedCounter() {
    trainerCreatedCounter.increment();
  }

  public void incrementTrainingCreatedCounter() {
    trainingCreatedCounter.increment();
  }
}
