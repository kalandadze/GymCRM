package com.example.gymcrm.model;

import lombok.*;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Trainee extends User {
  private LocalDate birthDate;
  private String address;
  private String userId;

  public Trainee(String firstName, String lastName, LocalDate birthDate, String address, String userId) {
    super(firstName, lastName);
    this.birthDate = birthDate;
    this.address = address;
    this.userId = userId;
  }

//  @Override
//  public boolean equals(Object o) {
//    if (o == null || getClass() != o.getClass()) return false;
//    if (!super.equals(o)) return false;
//    Trainee trainee = (Trainee) o;
//    return Objects.equals(birthDate, trainee.birthDate) && Objects.equals(address, trainee.address) && Objects.equals(userId, trainee.userId);
//  }
}
