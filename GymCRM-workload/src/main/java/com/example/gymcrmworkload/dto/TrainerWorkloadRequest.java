package com.example.gymcrmworkload.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
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
