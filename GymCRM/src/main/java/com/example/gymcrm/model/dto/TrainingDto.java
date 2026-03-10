package com.example.gymcrm.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TrainingDto {
  private String trainingName;
  private LocalDateTime trainingDate;
  private String trainingType;
  private Double duration;
  private String trainerName;
}
