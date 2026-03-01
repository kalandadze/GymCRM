package com.example.gymcrmworkload.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
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
