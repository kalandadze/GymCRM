package com.example.gymcrm.controller;

import com.example.gymcrm.model.LoginInfo;
import com.example.gymcrm.model.Trainee;
import com.example.gymcrm.model.Trainer;
import com.example.gymcrm.model.Training;
import com.example.gymcrm.model.dto.TraineeProfile;
import com.example.gymcrm.service.TraineeService;
import com.example.gymcrm.service.TrainerService;
import com.example.gymcrm.service.UserService;
import com.example.gymcrm.utils.ModelConverter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

@RestController
@RequestMapping("/trainees")
@Tag(name = "application/json", description = "Operations for creating, updating, retrieving and deleting trainees in the application")
@Slf4j
public class TraineeController {
    private final TraineeService service;
    private final TrainerService trainerService;
    private final UserService userService;


    public TraineeController(TraineeService service, TrainerService trainerService, UserService userService) {
        this.service = service;
        this.trainerService = trainerService;
        this.userService = userService;
    }

    @PostMapping
    @Operation(description = "add a trainee to the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully added the trainee"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "422", description = "Application failed due to receiving invalid field in the request"),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    }
    )
    public ResponseEntity<?> addTrainee(@RequestParam String firstName, @RequestParam String lastName, @RequestParam(required = false) LocalDate dateOfBirth, @RequestParam(required = false) String address) {
        Trainee trainee = new Trainee();
        trainee.setFirstName(firstName);
        trainee.setLastName(lastName);
        trainee.setAddress(address);
        if (dateOfBirth != null) trainee.setBirthDate(dateOfBirth);
        try {
            LoginInfo loginInfo = service.save(trainee);
            return ResponseEntity.ok().body(loginInfo);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(422).body(e.getMessage());
        }
    }

    @GetMapping
    @Operation(description = "get your trainee profile from the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully returned the trainee profile"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "422", description = "Application failed due to receiving invalid field in the request"),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    }
    )
    public ResponseEntity<?> getTraineeProfile(@AuthenticationPrincipal UserDetails user) {
        String username = user.getUsername();
        try {
            Trainee trainee = service.getTrainee(username);
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
    @Operation(description = "update your trainee profile from the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the trainee profile"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "422", description = "Application failed due to receiving invalid field in the request"),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    }
    )
    public ResponseEntity<?> updateTraineeProfile(@AuthenticationPrincipal UserDetails user,
                                                  @RequestParam String newUsername, @RequestParam String firstName, @RequestParam String lastName,
                                                  @RequestParam(required = false) LocalDate dateOfBirth, @RequestParam(required = false) String address, @RequestParam boolean isActive) {
        String username = user.getUsername();
        try {
            Trainee trainee = new Trainee();
            trainee.setUsername(newUsername);
            trainee.setFirstName(firstName);
            trainee.setLastName(lastName);
            trainee.setBirthDate(dateOfBirth);
            trainee.setAddress(address);
            trainee.setActive(isActive);
            service.updateTrainee(trainee, username);
            return ResponseEntity.ok().body(ModelConverter.convert(trainee));
        } catch (NoSuchElementException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(401).body(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(500).body("Application failed to process the request");
        }
    }

    @DeleteMapping
    @Operation(description = "delete your trainee profile from the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted the trainee profile"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "422", description = "Application failed due to receiving invalid field in the request"),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    }
    )
    public ResponseEntity<?> deleteTraineeProfile(@AuthenticationPrincipal UserDetails user) {
        String username = user.getUsername();
        try {
            service.deleteTrainee(username);
            return ResponseEntity.status(204).build();
        } catch (NoSuchElementException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(401).body(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(500).body("Application failed to process the request");
        }
    }

    @GetMapping("/trainers/not-assigned")
    @Operation(description = "get not assigned trainers to your trainee profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully returned the trainers profile"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "422", description = "Application failed due to receiving invalid field in the request"),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    }
    )
    public ResponseEntity<?> getNotAssignedTrainers(@AuthenticationPrincipal UserDetails user) {
        String username = user.getUsername();
        try {
            Trainee trainee = service.getTrainee(username);
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
    @Operation(description = "add trainers to your trainee profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully added the trainers profile"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "422", description = "Application failed due to receiving invalid field in the request"),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    }
    )
    @Transactional
    public ResponseEntity<?> updateTraineeTrainers(@AuthenticationPrincipal UserDetails user, @RequestBody List<String> trainersUsernames) {
        String username = user.getUsername();
        try {
            Trainee trainee = service.getTrainee(username);
            for (String trainerUsername : trainersUsernames) {
                Trainer trainer = trainerService.getTrainerByUsername(trainerUsername);
                trainee.getTrainers().add(trainer);
                trainer.getTrainees().add(trainee);
            }
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
    @Operation(description = "get not assigned trainers to your trainee profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully returned the trainers profile"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "422", description = "Application failed due to receiving invalid field in the request"),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    }
    )
    public ResponseEntity<?> getTraineeTrainings(@AuthenticationPrincipal UserDetails user, @RequestParam(required = false) LocalDateTime periodFrom, @RequestParam(required = false) LocalDateTime periodTo, @RequestParam(required = false) String trainerName, @RequestParam(required = false) String trainingType) {
        String username = user.getUsername();
        try {
            Trainee trainee = service.getTrainee(username);
//      List<Training> trainings = service.getTrainingsByUsernameAndCriteria(username, password, periodFrom, periodTo, trainerName, trainingType);
            Predicate<Training> predicate = s -> true;
            if (trainingType != null)
                predicate = predicate.and(s -> s.getTrainingType() != null && s.getTrainingType().getTrainingTypeName().equals(trainingType));
            if (trainerName != null)
                predicate = predicate.and(s -> s.getTrainer().getFirstName().equals(trainerName));
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

    @PatchMapping("/activity")
    @Operation(description = "activate or deactivate your trainee profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully activated/deactivated your trainee profile"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "422", description = "Application failed due to receiving invalid field in the request"),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    }
    )
    public ResponseEntity<?> changeActivity(@AuthenticationPrincipal UserDetails user, @RequestParam Boolean isActive) {
        String username = user.getUsername();
        try {
            service.changeActivity(username, isActive);
            return ResponseEntity.status(204).build();
        } catch (NoSuchElementException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(401).body(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(500).body("Application failed to process the request");
        }
    }

}
