package com.example.gymcrm.controller;

import com.example.gymcrm.model.Trainee;
import com.example.gymcrm.model.Trainer;
import com.example.gymcrm.model.Training;
import com.example.gymcrm.service.TraineeService;
import com.example.gymcrm.service.TrainerService;
import com.example.gymcrm.service.TrainingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
@Api(produces = "application/json", value = "Operations for creating, updating, retrieving and deleting trainings in the application")
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
  @ApiOperation(value = "add a training to the database")
  @ApiResponses(value = {
    @ApiResponse(code = 201, message = "Successfully added the trainee"),
    @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
    @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
    @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
    @ApiResponse(code = 422, message = "Application failed due to receiving invalid field in the request"),
    @ApiResponse(code = 500, message = "Application failed to process the request")
  }
  )
  public ResponseEntity<?> addTraining(@RequestParam String traineeUsername, @RequestParam String trainerUsername, @RequestParam String trainingName, @RequestParam LocalDateTime trainingDate, @RequestParam double trainingDuration) {
    try {
      Trainer trainer = trainerService.getTrainerByUsername(trainerUsername);
      Trainee trainee = traineeService.getTraineeByUsername(traineeUsername);

      Training training = new Training();
      training.setTraineeId(trainee);
      training.setTrainerId(trainer);
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
  @ApiOperation(value = "get all training types from the database")
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Successfully returned all training types"),
    @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
    @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
    @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
    @ApiResponse(code = 422, message = "Application failed due to receiving invalid field in the request"),
    @ApiResponse(code = 500, message = "Application failed to process the request")
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
