package com.example.gymcrm.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public abstract class User {
  private String firstName;
  private String lastName;
  private String username;
  private String password;
  private boolean isActive;

  public User(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }
}
