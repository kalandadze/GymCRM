package com.example.gymcrm.database;

import com.example.gymcrm.model.Trainer;
import com.example.gymcrm.model.User;
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
class TrainerDatabaseTest {
  @Autowired
  TrainerDatabase trainerDatabase;
  @Value("${trainer.file.path}")
  private String trainersFilePath;

  @Test
  void DatabaseLoadsTrainersCorrectly() throws IOException {
    Trainer[] loadedTrainers = trainerDatabase.getAllTrainers().toArray(Trainer[]::new);

    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    String json = Files.readString(Path.of(trainersFilePath));
    Trainer[] trainers = mapper.reader().readValue(json, Trainer[].class);

    Arrays.sort(trainers, Comparator.comparing(User::getUsername));
    Arrays.sort(loadedTrainers, Comparator.comparing(User::getUsername));
    assertArrayEquals(loadedTrainers, trainers);
  }
}