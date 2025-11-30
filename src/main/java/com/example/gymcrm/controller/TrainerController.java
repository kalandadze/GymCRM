package com.example.gymcrm.controller;

import com.example.gymcrm.model.LoginInfo;
import com.example.gymcrm.model.Trainer;
import com.example.gymcrm.model.Training;
import com.example.gymcrm.model.dto.TrainerProfile;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

@RestController
@RequestMapping("/trainers")
@Api(produces = "application/json", value = "Operations for creating, updating, retrieving and deleting trainers in the application")
@Slf4j
public class TrainerController {
  private final TrainerService service;

  public TrainerController(TrainerService service) {
    this.service = service;
  }

  @PostMapping
  @ApiOperation(value = "add a trainer to the database", response = Trainer.class)
  @ApiResponses(value = {
    @ApiResponse(code = 201, message = "Successfully added the trainee"),
    @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
    @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
    @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
    @ApiResponse(code = 422, message = "Application failed due to receiving invalid field in the request"),
    @ApiResponse(code = 500, message = "Application failed to process the request")
  }
  )
  public ResponseEntity<?> addTrainee(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String specialization) {
    Trainer trainer = new Trainer();
    trainer.setFirstName(firstName);
    trainer.setLastName(lastName);
    trainer.setSpecialization(specialization);
    try {
      trainer = service.save(trainer);
      LoginInfo loginInfo = new LoginInfo(trainer.getUsername(), trainer.getPassword());
      return ResponseEntity.ok().body(loginInfo);
    } catch (Exception e) {
      log.error(e.getMessage());
      return ResponseEntity.status(422).body(e.getMessage());
    }
  }


  @GetMapping
  @ApiOperation(value = "get your trainer profile from the database", response = TrainerProfile.class)
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Successfully returned the trainer profile"),
    @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
    @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
    @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
    @ApiResponse(code = 422, message = "Application failed due to receiving invalid field in the request"),
    @ApiResponse(code = 500, message = "Application failed to process the request")
  }
  )
  public ResponseEntity<?> getTrainerProfile(@CookieValue("username") String username, @CookieValue("password") String password) {
    username = decodeCookie(username);
    password = decodeCookie(password);
    try {
      Trainer trainer = service.getTrainer(username, password);
      return ResponseEntity.ok().body(ModelConverter.convert(trainer));
    } catch (NoSuchElementException e) {
      log.error(e.getMessage());
      return ResponseEntity.status(401).body(e.getMessage());
    } catch (Exception e) {
      log.error(e.getMessage());
      return ResponseEntity.status(500).body("Application failed to process the request");
    }
  }

  @PutMapping
  @ApiOperation(value = "update your trainer profile from the database", response = TrainerProfile.class)
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Successfully updated the trainer profile"),
    @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
    @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
    @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
    @ApiResponse(code = 422, message = "Application failed due to receiving invalid field in the request"),
    @ApiResponse(code = 500, message = "Application failed to process the request")
  }
  )
  public ResponseEntity<?> updateTrainerProfile(@CookieValue("username") String username, @CookieValue("password") String password,
                                                @RequestParam String newUsername, @RequestParam String firstName, @RequestParam String lastName,
                                                @RequestParam(required = false) String specialization, @RequestParam boolean isActive) {
    username = decodeCookie(username);
    password = decodeCookie(password);
    try {
      Trainer trainer = service.getTrainer(username, password);
      trainer.setUsername(newUsername);
      trainer.setFirstName(firstName);
      trainer.setLastName(lastName);
      if (specialization != null) trainer.setSpecialization(specialization);
      trainer.setActive(isActive);
      service.updateTrainer(trainer);
      ResponseCookie usernameCookie = encodedCookie("username", newUsername);
      return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, usernameCookie.toString()).body(ModelConverter.convert(trainer));
    } catch (NoSuchElementException e) {
      log.error(e.getMessage());
      return ResponseEntity.status(401).body(e.getMessage());
    } catch (Exception e) {
      log.error(e.getMessage());
      return ResponseEntity.status(500).body("Application failed to process the request");
    }
  }


  @GetMapping("/trainings")
  @ApiOperation(value = "get trainers trainings from the database", response = TrainerProfile.class)
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Successfully returned the trainings"),
    @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
    @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
    @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
    @ApiResponse(code = 422, message = "Application failed due to receiving invalid field in the request"),
    @ApiResponse(code = 500, message = "Application failed to process the request")
  }
  )
  public ResponseEntity<?> getTrainerTrainings(@CookieValue("username") String username, @CookieValue("password") String password, @RequestParam(required = false) LocalDateTime periodFrom, @RequestParam(required = false) LocalDateTime periodTo, @RequestParam(required = false) String traineeName) {
    username = decodeCookie(username);
    password = decodeCookie(password);
    try {
      Trainer trainer = service.getTrainer(username, password);
//      List<Training> trainings = service.getTrainingsByUsernameAndCriteria(username, password, periodFrom, periodTo, traineeName);
      Predicate<Training> predicate = s -> true;
      if (periodFrom != null) predicate = predicate.and(s -> s.getTrainingTime().isAfter(periodFrom));
      if (periodTo != null) predicate = predicate.and(s -> s.getTrainingTime().isBefore(periodTo));
      if (traineeName != null) predicate = predicate.and(s -> s.getTraineeId().getFirstName().equals(traineeName));
      List<Training> trainings = trainer.getTrainingList().stream().filter(predicate).toList();
      return ResponseEntity.ok().body(trainings.stream().map(ModelConverter::convert).toList());
    } catch (NoSuchElementException e) {
      log.error(e.getMessage());
      return ResponseEntity.status(401).body(e.getMessage());
    } catch (Exception e) {
      log.error(e.getMessage());
      return ResponseEntity.status(500).body("Application failed to process the request");
    }
  }


  @PatchMapping("/Activity")
  @ApiOperation(value = "activate or deactivate your trainer profile")
  @ApiResponses(value = {
    @ApiResponse(code = 204, message = "Successfully activated/deactivated your trainer profile"),
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
      service.updateActivity(username, password, isActive);
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
