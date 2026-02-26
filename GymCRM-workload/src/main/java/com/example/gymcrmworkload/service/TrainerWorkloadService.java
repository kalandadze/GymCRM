package com.example.gymcrmworkload.service;

import com.example.gymcrmworkload.dto.TrainerWorkloadRequest;
import com.example.gymcrmworkload.model.TrainerWorkload;
import com.example.gymcrmworkload.repository.TrainerWorkloadRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.ArrayList;

@Service
@Slf4j
public class TrainerWorkloadService {
    private final TrainerWorkloadRepository trainerRepository;

    public TrainerWorkloadService(TrainerWorkloadRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

    public void handleNewTrainerWorkload(TrainerWorkloadRequest trainerWorkloadRequest) {
        TrainerWorkload trainerWorkload = trainerRepository.findByUsername(trainerWorkloadRequest.getUsername()).orElse(
                TrainerWorkload.builder()
                        .username(trainerWorkloadRequest.getUsername())
                        .firstName(trainerWorkloadRequest.getFirstName())
                        .lastName(trainerWorkloadRequest.getLastName())
                        .isActive(trainerWorkloadRequest.isActive())
                        .trainingSummery(new ArrayList<>())
                        .build()
        );

        Month month = trainerWorkloadRequest.getTrainingDate().getMonth();
        int year = trainerWorkloadRequest.getTrainingDate().getYear();
        TrainerWorkload.YearlyWorkloadOverview yearlyWorkloadOverview = trainerWorkload.getYearlyWorkloadOverview(year);

        yearlyWorkloadOverview.addMonthlyWorkload(month,
                ((trainerWorkloadRequest.getActionType().equals(TrainerWorkloadRequest.ActionType.ADD) ? 1 : -1) * trainerWorkloadRequest.getDuration())
        );
        trainerRepository.save(trainerWorkload);
    }


    public TrainerWorkload getTrainersWorkloadSummery(String username) {
        return trainerRepository.findByUsername(username).orElseThrow();
    }
}
