package com.example.gymcrmworkload.repository.repositoryImplementation;

import com.example.gymcrmworkload.database.TrainerDatabase;
import com.example.gymcrmworkload.model.TrainerWorkload;
import com.example.gymcrmworkload.repository.TrainerRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class TrainerRepositoryImplementation implements TrainerRepository {
    private final TrainerDatabase trainerDatabase;

    public TrainerRepositoryImplementation(TrainerDatabase trainerDatabase) {
        this.trainerDatabase = trainerDatabase;
    }

    @Override
    public void save(TrainerWorkload trainerWorkload) {
        findByUsername(trainerWorkload.getUsername()).ifPresent(load -> trainerDatabase.getTrainers().remove(load));
        trainerDatabase.getTrainers().add(trainerWorkload);
    }

    @Override
    public Optional<TrainerWorkload> findByUsername(String username) {
        return trainerDatabase.getTrainers().stream().filter(trainer -> trainer.getUsername().equals(username)).findFirst();
    }
}
