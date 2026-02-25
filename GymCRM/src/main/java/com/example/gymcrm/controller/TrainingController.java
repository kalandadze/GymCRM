package com.example.gymcrm.controller;

import com.example.gymcrm.model.Trainee;
import com.example.gymcrm.model.Trainer;
import com.example.gymcrm.model.Training;
import com.example.gymcrm.service.TraineeService;
import com.example.gymcrm.service.TrainerService;
import com.example.gymcrm.service.TrainingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Slf4j
@RestController
@RequestMapping("/trainings")
@Tag(name = "application/json", description = "Operations for creating, updating, retrieving and deleting trainings in the application")
public class TrainingController {
    private final TrainingService service;
    private final TrainerService trainerService;
    private final TraineeService traineeService;

    public TrainingController(TrainingService service, TrainerService trainerService, TraineeService traineeService) {
        this.service = service;
        this.trainerService = trainerService;
        this.traineeService = traineeService;
    }


    @PostMapping
    @Operation(summary = "add a training to the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully added the trainee"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "422", description = "Application failed due to receiving invalid field in the request"),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    }
    )
    public ResponseEntity<?> addTraining(@RequestParam String traineeUsername, @RequestParam String trainerUsername, @RequestParam String trainingName, @RequestParam LocalDateTime trainingDate, @RequestParam double trainingDuration) {
        try {
            Trainer trainer = trainerService.getTrainerByUsername(trainerUsername);
            Trainee trainee = traineeService.getTraineeByUsername(traineeUsername);

            Training training = new Training();
            training.setTrainee(trainee);
            training.setTrainer(trainer);
            training.setTrainingName(trainingName);
            training.setTrainingTime(trainingDate);
            training.setDuration(trainingDuration);

            trainee.getTrainings().add(training);
            trainer.getTrainingList().add(training);
            service.save(training);
            return ResponseEntity.status(201).build();
        } catch (NoSuchElementException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(422).body(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(500).body("Application failed to process the request");
        }
    }

    @GetMapping("/types")
    @Operation(summary = "get all training types from the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully returned all training types"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "422", description = "Application failed due to receiving invalid field in the request"),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    }
    )
    public ResponseEntity<?> getTrainingTypes() {
        try {
            return ResponseEntity.status(200).body(service.getAllTrainingTypes());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(500).body("Application failed to process the request");
        }
    }


    private String decodeCookie(String encodedCookie) {
        return URLDecoder.decode(encodedCookie, StandardCharsets.UTF_8);
    }
}
