package com.example.gymcrm.service;

import com.example.gymcrm.model.Trainer;
import com.example.gymcrm.model.Training;
import com.example.gymcrm.model.dto.TrainerWorkloadRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class WorkloadService {
    private final RestTemplate restTemplate;

    public WorkloadService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @CircuitBreaker(name = "workloadService", fallbackMethod = "fallbackMethod")
    public void notifyNewTrainerWorkload(Training training, TrainerWorkloadRequest.ActionType actionType) {
        Trainer trainer = training.getTrainer();
        TrainerWorkloadRequest trainerWorkloadRequest = TrainerWorkloadRequest.builder()
                .username(trainer.getUsername())
                .firstName(trainer.getFirstName())
                .lastName(trainer.getLastName())
                .isActive(trainer.isActive())
                .trainingDate(training.getTrainingTime())
                .duration(training.getDuration())
                .actionType(actionType)
                .build();

        HttpEntity<TrainerWorkloadRequest> entity = new HttpEntity<>(trainerWorkloadRequest);
        restTemplate.exchange(
                "http://GymCRM-workload/workload/trainer",
                HttpMethod.POST,
                entity,
                Void.class
        );

        log.info("Workload service notified successfully");
    }

    public void fallbackMethod(Training training, TrainerWorkloadRequest.ActionType actionType, Throwable e) {
        log.error("Workload service unavailable: {}", e.getMessage());
    }
}
