package com.example.gymcrmworkload.steps;

import com.example.gymcrmworkload.dto.TrainerWorkloadRequest;
import com.example.gymcrmworkload.model.TrainerWorkload;
import com.example.gymcrmworkload.repository.TrainerWorkloadRepository;
import com.example.gymcrmworkload.service.TrainerWorkloadService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WorkloadSteps {

    @Autowired
    private TrainerWorkloadRepository repository;

    @Autowired
    private TrainerWorkloadService workloadService;

    private boolean failed;
    private TrainerWorkload savedWorkload;
    private TrainerWorkloadRequest trainerWorkloadRequest = TrainerWorkloadRequest.builder()
            .actionType(TrainerWorkloadRequest.ActionType.ADD)
            .trainingDate(LocalDateTime.of(2026, Month.JANUARY, 5, 1, 20))
            .firstName("fname")
            .lastName("lname").build();

    @Given("a trainer workload request with trainer {string} and duration {int}")
    public void workloadRequest(String trainer, int duration) {
        trainerWorkloadRequest.setUsername(trainer);
        trainerWorkloadRequest.setDuration((double) duration);
    }

    @Given("a trainer {string} already has workload {int}")
    public void trainerAlreadyHasWorkload(String trainer, int workload) {

        TrainerWorkload.YearlyWorkloadOverview overview = new TrainerWorkload.YearlyWorkloadOverview();
        overview.addMonthlyWorkload(Month.JANUARY, (double) workload);
        Map<Integer, TrainerWorkload.YearlyWorkloadOverview> overviewMap = new HashMap<>();
        overviewMap.put(2026, overview);

        TrainerWorkload existing = TrainerWorkload.builder()
                .username(trainer)
                .trainingSummery(overviewMap)
                .build();

        Mockito.when(repository.findByUsername(trainer))
                .thenReturn(Optional.of(existing));
    }

    @Given("an invalid workload request")
    public void invalidRequest() {
    }

    @When("the workload request is processed")
    public void processRequest() {
        try {
            Mockito.when(repository.save(Mockito.any()))
                    .thenAnswer(invocation -> {
                        savedWorkload = invocation.getArgument(0);
                        return savedWorkload;
                    });
            workloadService.handleNewTrainerWorkload(trainerWorkloadRequest);
        } catch (Exception e) {
            failed = true;
        }
    }

    @Then("the workload for trainer {string} should be {int}")
    public void verifyWorkload(String trainer, int expected) {
        assertEquals(expected, savedWorkload.getTrainingSummery().get(2026).getMonthlyWorkload(Month.JANUARY));
    }

    @Then("the workload request should fail")
    public void workloadShouldFail() {
        assertTrue(failed);
    }
}