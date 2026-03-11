package com.example.gymcrmworkload.controller;

import com.example.gymcrmworkload.dto.TrainerWorkloadRequest;
import com.example.gymcrmworkload.service.TrainerWorkloadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/workload/trainer")
@Slf4j
public class TrainerWorkloadController {
    private final TrainerWorkloadService trainerWorkloadService;

    public TrainerWorkloadController(TrainerWorkloadService trainerWorkloadService) {
        this.trainerWorkloadService = trainerWorkloadService;
    }

    @PostMapping
    public ResponseEntity<?> updateTrainerWorkload(@RequestBody TrainerWorkloadRequest trainerWorkloadRequest) {
        try {
            trainerWorkloadService.handleNewTrainerWorkload(trainerWorkloadRequest);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> getTrainersWorkloadSummery(@PathVariable String username) {
        try {
            var summery = trainerWorkloadService.getTrainersWorkloadSummery(username);
            return ResponseEntity.ok().body(summery);
        } catch (NoSuchElementException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body("Trainer with username " + username + " not found");
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }


}
