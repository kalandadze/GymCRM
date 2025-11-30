package com.example.gymcrm.utils;

import com.example.gymcrm.model.Trainee;
import com.example.gymcrm.model.Trainer;
import com.example.gymcrm.model.Training;
import com.example.gymcrm.model.dto.TraineeProfile;
import com.example.gymcrm.model.dto.TrainerProfile;
import com.example.gymcrm.model.dto.TrainingDto;

public class ModelConverter {
  public static TraineeProfile convert(Trainee trainee) {
    return TraineeProfile.builder()
      .firstName(trainee.getFirstName())
      .lastName(trainee.getLastName())
      .dateOfBirth(trainee.getBirthDate())
      .Address(trainee.getAddress())
      .IsActive(trainee.isActive())
      .trainersList(trainee.getTrainers().stream().map(ModelConverter::convertTrainer).toList()).build();
  }

  public static TraineeProfile.TrainerProfile convertTrainer(Trainer trainer) {
    return TraineeProfile.TrainerProfile.builder()
      .username(trainer.getUsername())
      .firstName(trainer.getFirstName())
      .lastName(trainer.getLastName())
      .specialization(trainer.getSpecialization()).build();
  }

  public static TrainerProfile convert(Trainer trainer) {
    return TrainerProfile.builder()
      .firstName(trainer.getFirstName())
      .lastName(trainer.getLastName())
      .specialization(trainer.getSpecialization())
      .IsActive(trainer.isActive())
      .traineesList(trainer.getTrainees().stream().map(ModelConverter::convertTrainees).toList()).build();
  }

  public static TrainerProfile.TraineeProfile convertTrainees(Trainee trainer) {
    return TrainerProfile.TraineeProfile.builder()
      .username(trainer.getUsername())
      .firstName(trainer.getFirstName())
      .lastName(trainer.getLastName()).build();
  }

  public static TrainingDto convert(Training training) {
    return TrainingDto.builder()
      .trainingName(training.getTrainingName())
      .trainingDate(training.getTrainingTime())
      .trainingType(training.getTrainingType() == null ? null : training.getTrainingType().getTrainingTypeName())
      .duration(training.getDuration())
      .trainerName(training.getTrainerId().getFirstName()).build();
  }
}
