package com.example.gymcrm.database;

import com.example.gymcrm.model.Trainee;
import com.example.gymcrm.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TraineeDatabaseTest {
  @Autowired
  TraineeDatabase traineeDatabase;
  @Value("${trainee.file.path}")
  private String traineesFilePath;

  @Test
  void DatabaseLoadsTraineesCorrectly() throws IOException {
    Trainee[] loadedTrainees = traineeDatabase.getAllTrainees().toArray(Trainee[]::new);

    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    String json = Files.readString(Path.of(traineesFilePath));
    Trainee[] trainees = mapper.reader().readValue(json, Trainee[].class);

    Arrays.sort(trainees, Comparator.comparing(User::getUsername));
    Arrays.sort(loadedTrainees, Comparator.comparing(User::getUsername));
    assertArrayEquals(loadedTrainees, trainees);
  }
}