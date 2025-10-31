package com.example.gymcrm.database;

import com.example.gymcrm.model.Trainee;
import com.example.gymcrm.model.Training;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

@SpringBootTest
class TrainingDatabaseTest {
  @Autowired
  TrainingDatabase trainingDatabase;
  @Value("${training.file.path}")
  private String trainingsFilePath;

  @Test
  void DatabaseLoadsTrainingsCorrectly() throws IOException {
    Training[] loadedTrainings = trainingDatabase.getAllTrainings().toArray(Training[]::new);

    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    String json = Files.readString(Path.of(trainingsFilePath));
    Training[] trainings = mapper.reader().readValue(json, Training[].class);

    Arrays.sort(trainings, Comparator.comparing(Training::getTrainingName));
    Arrays.sort(loadedTrainings, Comparator.comparing(Training::getTrainingName));
    assertArrayEquals(loadedTrainings, trainings);
  }
}