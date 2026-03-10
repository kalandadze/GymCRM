package com.example.gymcrm.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class TraineeProfile {
  private String firstName;
  private String lastName;
  private LocalDate dateOfBirth;
  private String Address;
  private boolean IsActive;
  private List<TrainerProfile> trainersList;

  @Data
  @Builder
  public static class TrainerProfile {
    private String username;
    private String firstName;
    private String lastName;
    private String specialization;
  }
}
