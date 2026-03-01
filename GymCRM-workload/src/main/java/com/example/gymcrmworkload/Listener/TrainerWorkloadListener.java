package com.example.gymcrmworkload.Listener;

import com.example.gymcrmworkload.dto.TrainerWorkloadRequest;
import com.example.gymcrmworkload.service.TrainerWorkloadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@Slf4j
public class TrainerWorkloadListener {
    private final TrainerWorkloadService trainerWorkloadService;
    private final JmsTemplate jmsTemplate;
    private final static String INVALID_REQUEST_QUEUE = "trainer.workload.invalid.request";

    public TrainerWorkloadListener(TrainerWorkloadService trainerWorkloadService, JmsTemplate jmsTemplate) {
        this.trainerWorkloadService = trainerWorkloadService;
        this.jmsTemplate = jmsTemplate;
    }

    @JmsListener(destination = "trainer.workload.queue")
    public void listen(TrainerWorkloadRequest trainerWorkloadRequest) {
        log.info("New TrainerWorkloadRequest received");
        try {
            validate(trainerWorkloadRequest);
            trainerWorkloadService.handleNewTrainerWorkload(trainerWorkloadRequest);
        } catch (Exception e) {
            jmsTemplate.convertAndSend(INVALID_REQUEST_QUEUE, trainerWorkloadRequest);
        }
    }

    private void validate(TrainerWorkloadRequest trainerWorkloadRequest) {
        if (trainerWorkloadRequest.getUsername().isEmpty()||trainerWorkloadRequest.getTrainingDate().isAfter(LocalDateTime.now())){
            throw new IllegalArgumentException("Invalid TrainerWorkloadRequest");
        }
    }
}
