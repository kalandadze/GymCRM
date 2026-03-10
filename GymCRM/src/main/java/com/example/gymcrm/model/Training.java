package com.example.gymcrm.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "trainings")
public class Training {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String trainingName;
  @ManyToOne(cascade = CascadeType.PERSIST)
  private Trainee trainee;
  @ManyToOne
  private Trainer trainer;
  @ManyToOne
  private TrainingType trainingType;
  private LocalDateTime trainingTime;
  private Double duration;

  public Training(String trainingName, Trainee trainee, Trainer trainer, TrainingType trainingType, LocalDateTime trainingTime, Double duration) {
    this.trainingName = trainingName;
    this.trainee = trainee;
    this.trainer = trainer;
    this.trainingType = trainingType;
    this.trainingTime = trainingTime;
    this.duration = duration;
  }
}
