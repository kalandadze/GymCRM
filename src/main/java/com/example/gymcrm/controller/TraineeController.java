package com.example.gymcrm.controller;

import com.example.gymcrm.model.LoginInfo;
import com.example.gymcrm.model.Trainee;
import com.example.gymcrm.model.Trainer;
import com.example.gymcrm.model.Training;
import com.example.gymcrm.model.dto.TraineeProfile;
import com.example.gymcrm.service.TraineeService;
import com.example.gymcrm.service.TrainerService;
import com.example.gymcrm.utils.ModelConverter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

@RestController
@RequestMapping("/trainees")
@Api(produces = "application/json", value = "Operations for creating, updating, retrieving and deleting trainees in the application")
@Slf4j
public class TraineeController {
  private final TraineeService service;
  private final TrainerService trainerService;


  public TraineeController(TraineeService service, TrainerService trainerService) {
    this.service = service;
    this.trainerService = trainerService;
  }

  @PostMapping
  @ApiOperation(value = "add a trainee to the database", response = Trainee.class)
  @ApiResponses(value = {
    @ApiResponse(code = 201, message = "Successfully added the trainee"),
    @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
    @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
    @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
    @ApiResponse(code = 422, message = "Application failed due to receiving invalid field in the request"),
    @ApiResponse(code = 500, message = "Application failed to process the request")
  }
  )
  public ResponseEntity<?> addTrainee(@RequestParam String firstName, @RequestParam String lastName, @RequestParam(required = false) LocalDate dateOfBirth, @RequestParam(required = false) String address) {
    Trainee trainee = new Trainee();
    trainee.setFirstName(firstName);
    trainee.setLastName(lastName);
    trainee.setAddress(address);
    if (dateOfBirth != null) trainee.setBirthDate(dateOfBirth);
    try {
      trainee = service.save(trainee);
      LoginInfo loginInfo = new LoginInfo(trainee.getUsername(), trainee.getPassword());
      return ResponseEntity.ok().body(loginInfo);
    } catch (Exception e) {
      log.error(e.getMessage());
      return ResponseEntity.status(422).body(e.getMessage());
    }
  }

  @GetMapping
  @ApiOperation(value = "get your trainee profile from the database", response = TraineeProfile.class)
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Successfully returned the trainee profile"),
    @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
    @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
    @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
    @ApiResponse(code = 422, message = "Application failed due to receiving invalid field in the request"),
    @ApiResponse(code = 500, message = "Application failed to process the request")
  }
  )
  public ResponseEntity<?> getTraineeProfile(@CookieValue("username") String username, @CookieValue("password") String password) {
    username = decodeCookie(username);
    password = decodeCookie(password);
    try {
      Trainee trainee = service.getTrainee(username, password);
      return ResponseEntity.ok().body(ModelConverter.convert(trainee));
    } catch (NoSuchElementException e) {
      log.error(e.getMessage());
      return ResponseEntity.status(401).body(e.getMessage());
    } catch (Exception e) {
      log.error(e.getMessage());
      return ResponseEntity.status(500).body("Application failed to process the request");
    }
  }

  @PutMapping
  @ApiOperation(value = "update your trainee profile from the database", response = TraineeProfile.class)
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Successfully updated the trainee profile"),
    @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
    @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
    @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
    @ApiResponse(code = 422, message = "Application failed due to receiving invalid field in the request"),
    @ApiResponse(code = 500, message = "Application failed to process the request")
  }
  )
  public ResponseEntity<?> updateTraineeProfile(@CookieValue("username") String username, @CookieValue("password") String password,
                                                @RequestParam String newUsername, @RequestParam String firstName, @RequestParam String lastName,
                                                @RequestParam(required = false) LocalDate dateOfBirth, @RequestParam(required = false) String address, @RequestParam boolean isActive) {
    username = decodeCookie(username);
    password = decodeCookie(password);
    try {
      Trainee trainee = service.getTrainee(username, password);
      trainee.setUsername(newUsername);
      trainee.setFirstName(firstName);
      trainee.setLastName(lastName);
      if (dateOfBirth != null)
        trainee.setBirthDate(dateOfBirth);
      if (address != null) trainee.setAddress(address);
      trainee.setActive(isActive);
      service.updateTrainee(trainee);
      ResponseCookie usernameCookie = encodedCookie("username", newUsername);
      return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, usernameCookie.toString()).body(ModelConverter.convert(trainee));
    } catch (NoSuchElementException e) {
      log.error(e.getMessage());
      return ResponseEntity.status(401).body(e.getMessage());
    } catch (Exception e) {
      log.error(e.getMessage());
      return ResponseEntity.status(500).body("Application failed to process the request");
    }
  }

  @DeleteMapping
  @ApiOperation(value = "delete your trainee profile from the database")
  @ApiResponses(value = {
    @ApiResponse(code = 204, message = "Successfully deleted the trainee profile"),
    @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
    @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
    @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
    @ApiResponse(code = 422, message = "Application failed due to receiving invalid field in the request"),
    @ApiResponse(code = 500, message = "Application failed to process the request")
  }
  )
  public ResponseEntity<?> deleteTraineeProfile(@CookieValue("username") String username, @CookieValue("password") String password) {
    username = decodeCookie(username);
    password = decodeCookie(password);
    try {
      service.deleteTrainee(username, password);
      return ResponseEntity.status(204).body("Successfully deleted the trainee profile");
    } catch (NoSuchElementException e) {
      log.error(e.getMessage());
      return ResponseEntity.status(401).body(e.getMessage());
    } catch (Exception e) {
      log.error(e.getMessage());
      return ResponseEntity.status(500).body("Application failed to process the request");
    }
  }

  @GetMapping("/trainers/not-assigned")
  @ApiOperation(value = "get not assigned trainers to your trainee profile")
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Successfully returned the trainers profile"),
    @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
    @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
    @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
    @ApiResponse(code = 422, message = "Application failed due to receiving invalid field in the request"),
    @ApiResponse(code = 500, message = "Application failed to process the request")
  }
  )
  public ResponseEntity<?> getNotAssignedTrainers(@CookieValue("username") String username, @CookieValue("password") String password) {
    username = decodeCookie(username);
    password = decodeCookie(password);
    try {
      Trainee trainee = service.getTrainee(username, password);
      List<TraineeProfile.TrainerProfile> trainers = trainerService.getAllTrainers().stream().filter(trainer -> !trainer.getTrainees().contains(trainee)).map(ModelConverter::convertTrainer).toList();
      return ResponseEntity.status(200).body(trainers);
    } catch (NoSuchElementException e) {
      log.error(e.getMessage());
      return ResponseEntity.status(401).body(e.getMessage());
    } catch (Exception e) {
      log.error(e.getMessage());
      return ResponseEntity.status(500).body("Application failed to process the request");
    }
  }


  @PutMapping("/trainers")
  @ApiOperation(value = "add trainers to your trainee profile")
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Successfully added the trainers profile"),
    @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
    @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
    @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
    @ApiResponse(code = 422, message = "Application failed due to receiving invalid field in the request"),
    @ApiResponse(code = 500, message = "Application failed to process the request")
  }
  )
  public ResponseEntity<?> getNotAssignedTrainers(@CookieValue("username") String username, @CookieValue("password") String password, @RequestBody List<String> trainersUsernames) {
    username = decodeCookie(username);
    password = decodeCookie(password);
    try {
      Trainee trainee = service.getTrainee(username, password);
      for (String trainerUsername : trainersUsernames) {
        Trainer trainer = trainerService.getTrainerByUsername(trainerUsername);
        trainee.getTrainers().add(trainer);
      }
      service.updateTrainee(trainee);
      return ResponseEntity.status(200).body(trainee.getTrainers().stream().map(ModelConverter::convertTrainer).toList());
    } catch (NoSuchElementException e) {
      log.error(e.getMessage());
      return ResponseEntity.status(401).body(e.getMessage());
    } catch (Exception e) {
      log.error(e.getMessage());
      return ResponseEntity.status(500).body("Application failed to process the request");
    }
  }

  @GetMapping("/trainings")
  @ApiOperation(value = "get not assigned trainers to your trainee profile")
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Successfully returned the trainers profile"),
    @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
    @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
    @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
    @ApiResponse(code = 422, message = "Application failed due to receiving invalid field in the request"),
    @ApiResponse(code = 500, message = "Application failed to process the request")
  }
  )
  public ResponseEntity<?> getTraineeTrainings(@CookieValue("username") String username, @CookieValue("password") String password, @RequestParam(required = false) LocalDateTime periodFrom, @RequestParam(required = false) LocalDateTime periodTo, @RequestParam(required = false) String trainerName, @RequestParam(required = false) String trainingType) {
    username = decodeCookie(username);
    password = decodeCookie(password);
    try {
      Trainee trainee = service.getTrainee(username, password);
//      List<Training> trainings = service.getTrainingsByUsernameAndCriteria(username, password, periodFrom, periodTo, trainerName, trainingType);
      Predicate<Training> predicate = s -> true;
      if (trainingType != null) predicate = predicate.and(s -> s.getTrainingType() != null && s.getTrainingType().getTrainingTypeName().equals(trainingType));
      if (trainerName != null) predicate = predicate.and(s -> s.getTrainerId().getFirstName().equals(trainerName));
      if (periodFrom != null) predicate = predicate.and(s -> s.getTrainingTime().isAfter(periodFrom));
      if (periodTo != null) predicate = predicate.and(s -> s.getTrainingTime().isBefore(periodTo));
      List<Training> trainings = trainee.getTrainings().stream().filter(predicate).toList();
      return ResponseEntity.status(200).body(trainings.stream().map(ModelConverter::convert).toList());
    } catch (NoSuchElementException e) {
      log.error(e.getMessage());
      return ResponseEntity.status(401).body(e.getMessage());
    } catch (Exception e) {
      log.error(e.getMessage());
      return ResponseEntity.status(500).body("Application failed to process the request");
    }
  }

  @PatchMapping("/Activity")
  @ApiOperation(value = "activate or deactivate your trainee profile")
  @ApiResponses(value = {
    @ApiResponse(code = 204, message = "Successfully activated/deactivated your trainee profile"),
    @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
    @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
    @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
    @ApiResponse(code = 422, message = "Application failed due to receiving invalid field in the request"),
    @ApiResponse(code = 500, message = "Application failed to process the request")
  }
  )
  public ResponseEntity<?> changeActivity(@CookieValue("username") String username, @CookieValue("password") String password, @RequestParam Boolean isActive) {
    username = decodeCookie(username);
    password = decodeCookie(password);
    try {
      service.changeActivity(username, password, isActive);
      return ResponseEntity.status(204).build();
    } catch (NoSuchElementException e) {
      log.error(e.getMessage());
      return ResponseEntity.status(401).body(e.getMessage());
    } catch (Exception e) {
      log.error(e.getMessage());
      return ResponseEntity.status(500).body("Application failed to process the request");
    }
  }

  private String decodeCookie(String encodedCookie) {
    return URLDecoder.decode(encodedCookie, StandardCharsets.UTF_8);
  }

  private ResponseCookie encodedCookie(String contentName, String contentValue) {
    return ResponseCookie.from(contentName, URLEncoder.encode(contentValue, StandardCharsets.UTF_8)).httpOnly(true).path("/").build();
  }

}
