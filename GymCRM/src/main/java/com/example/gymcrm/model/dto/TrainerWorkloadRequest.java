package com.example.gymcrm.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
@Builder
public class TrainerWorkloadRequest {
    private String firstName;
    private String lastName;
    private String username;
    private boolean isActive;
    private LocalDateTime trainingDate;
    private Double duration;
    private ActionType actionType;

    public enum ActionType {
        ADD, DELETE
    }
}
