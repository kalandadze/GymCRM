package com.example.gymcrm.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "trainees")
public class Trainee extends GymUser {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private LocalDate birthDate;
  private String address;
  private String userId;
  @OneToMany(mappedBy = "trainee")
  private List<Training> trainings;
  @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
  @JoinTable(name = "trainee_trainer",
    joinColumns = @JoinColumn(name = "trainee_id"),
    inverseJoinColumns = {@JoinColumn(name = "trainer_id")})
  private List<Trainer> trainers;

  public Trainee(String firstName, String lastName, LocalDate birthDate, String address, String userId) {
    super(firstName, lastName);
    this.birthDate = birthDate;
    this.address = address;
    this.userId = userId;
  }
}

