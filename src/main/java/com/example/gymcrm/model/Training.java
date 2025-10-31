package com.example.gymcrm.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Training {
  private String trainingName;
  private String traineeId;
  private String trainerId;
  private TrainingType trainingType;
  private LocalDateTime trainingTime;
  private Duration duration;

//  @Override
//  public boolean equals(Object o) {
//    if (o == null || getClass() != o.getClass()) return false;
//    Training training = (Training) o;
//    return Objects.equals(trainingName, training.trainingName) && Objects.equals(traineeId, training.traineeId) && Objects.equals(trainerId, training.trainerId) && Objects.equals(trainingType, training.trainingType) && Objects.equals(trainingTime, training.trainingTime) && Objects.equals(duration, training.duration);
//  }
}
