package com.example.gymcrm.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Trainer extends User {
  private String specialization;
  private String userId;
  private TrainingType trainingType;

  public Trainer(String firstName, String lastName, String specialization, String userId, TrainingType trainingType) {
    super(firstName, lastName);
    this.specialization = specialization;
    this.userId = userId;
    this.trainingType = trainingType;
  }

//  @Override
//  public boolean equals(Object o) {
//    if (o == null || getClass() != o.getClass()) return false;
//    if (!super.equals(o)) return false;
//    Trainer trainer = (Trainer) o;
//    return Objects.equals(specialization, trainer.specialization) && Objects.equals(userId, trainer.userId) && Objects.equals(trainingType, trainer.trainingType);
//  }
}
