package com.example.gymcrm.database;

import com.example.gymcrm.model.Trainee;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Stream;

@Slf4j
@Component
public class TraineeDatabase {
  private final HashMap<String, Trainee> traineeHashMap = new HashMap<>();

  @Value("${trainee.file.path}")
  private String traineesFilePath;

  @PostConstruct
  private void loadTraineesDatabase(){
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    try {
      String json = Files.readString(Path.of(traineesFilePath));
      Arrays.stream(mapper.reader().readValue(json, Trainee[].class)).forEach(this::addTrainee);
    } catch (IOException e) {
      log.error(e.getMessage());
      throw new RuntimeException(e);
    }
  }

  public void addTrainee(Trainee trainee) {
    traineeHashMap.put(trainee.getUsername(), trainee);
  }

  public Trainee getTrainee(String username) {
    return traineeHashMap.get(username);
  }

  public Stream<Trainee> getAllTrainees() {
    return traineeHashMap.values().stream();
  }

  public void deleteTrainee(Trainee trainee) {
    traineeHashMap.remove(trainee.getUsername());
  }
}
