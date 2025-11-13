package com.example.gymcrm.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Immutable;

@Data
@NoArgsConstructor
@Entity
@Table(name = "traingTypes")
@Immutable
public class TrainingType {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private String trainingTypeName;

  public TrainingType(String trainingTypeName) {
    this.trainingTypeName = trainingTypeName;
  }
}
