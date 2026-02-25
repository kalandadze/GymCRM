package com.example.gymcrmworkload.dto;

import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
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
