package com.example.gymcrm.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "trainers")
public class Trainer extends User {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private String specialization;
  private String userId;
  @ManyToOne
  private TrainingType trainingType;
  @OneToMany(mappedBy = "trainerId")
  private List<Training> trainingList;
  @ManyToMany(mappedBy = "trainers")
  private List<Trainee> trainees;

  public Trainer(String firstName, String lastName, String specialization, String userId, TrainingType trainingType) {
    super(firstName, lastName);
    this.specialization = specialization;
    this.userId = userId;
    this.trainingType = trainingType;
  }
}
