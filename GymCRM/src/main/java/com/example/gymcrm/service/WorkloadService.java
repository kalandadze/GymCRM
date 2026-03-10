package com.example.gymcrm.service;

import com.example.gymcrm.model.Trainer;
import com.example.gymcrm.model.Training;
import com.example.gymcrm.model.dto.TrainerWorkloadRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WorkloadService {
    private final String TRAINER_WORKLOAD_QUEUE = "trainer.workload.queue";
    private final JmsTemplate jmsTemplate;

    public WorkloadService(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

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
        jmsTemplate.convertAndSend(TRAINER_WORKLOAD_QUEUE, trainerWorkloadRequest);
        log.info("message to workload service sent");
    }
}
