package com.example.gymcrmworkload.repository;

import com.example.gymcrmworkload.model.TrainerWorkload;

import java.util.Optional;

public interface TrainerRepository {
    void save(TrainerWorkload trainerWorkload);
    Optional<TrainerWorkload> findByUsername(String username);
}
