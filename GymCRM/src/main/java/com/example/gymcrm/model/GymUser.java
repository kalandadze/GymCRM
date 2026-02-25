package com.example.gymcrm.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class GymUser {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private String firstName;
  private String lastName;
  private String username;
  private String password;
  private boolean isActive = false;
  private Integer loginTries;
  private LocalDateTime blockedUntil;

  public GymUser(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }
}
