package com.example.gymcrm.database;

import com.example.gymcrm.model.Trainer;
import com.example.gymcrm.model.Training;
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
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Component
public class TrainingDatabase {
  private final HashMap<Integer, Training> trainingHashMap = new HashMap<>();
  private int id = 0;
  @Value("${training.file.path}")
  private String trainingsFilePath;

  @PostConstruct
  private void loadTrainingsDatabase(){
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    try {
      String json = Files.readString(Path.of(trainingsFilePath));
      Arrays.stream(mapper.reader().readValue(json, Training[].class)).forEach(this::addTraining);
    } catch (IOException e) {
      log.error(e.getMessage());
      throw new RuntimeException(e);
    }
  }

  public void addTraining(Training training) {
    trainingHashMap.put(id++, training);
  }

  public List<Training> getAllTrainings() {
    return trainingHashMap.values().stream().toList();
  }

  public Stream<Training> getAllTrainingStream() {
    return trainingHashMap.values().stream();
  }
}
