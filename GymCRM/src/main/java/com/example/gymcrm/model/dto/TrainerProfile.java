package com.example.gymcrm.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TrainerProfile {
  private String firstName;
  private String lastName;
  private String specialization;
  private boolean IsActive;
  private List<TraineeProfile> traineesList;

  @Data
  @Builder
  public static class TraineeProfile {
    private String username;
    private String firstName;
    private String lastName;
  }
}
