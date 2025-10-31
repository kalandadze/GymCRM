package com.example.gymcrm.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class TrainingType {
  private String trainingTypeName;

//  @Override
//  public boolean equals(Object o) {
//    if (o == null || getClass() != o.getClass()) return false;
//    TrainingType that = (TrainingType) o;
//    return Objects.equals(trainingTypeName, that.trainingTypeName);
//  }
}
