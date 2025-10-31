package com.example.gymcrm.database;

import com.example.gymcrm.model.Trainer;
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
public class TrainerDatabase {
  private final HashMap<String, Trainer> trainerHashMap = new HashMap<>();
  @Value("${trainer.file.path}")
  private String trainersFilePath;

  @PostConstruct
  private void loadTrainersDatabase(){
    ObjectMapper mapper = new ObjectMapper();
    try {
      String json = Files.readString(Path.of(trainersFilePath));
      Arrays.stream(mapper.reader().readValue(json, Trainer[].class)).forEach(this::addTrainer);
    } catch (IOException e) {
      log.error(e.getMessage());
      throw new RuntimeException(e);
    }
  }

  public void addTrainer(Trainer trainer) {
    trainerHashMap.put(trainer.getUsername(), trainer);
  }

  public Trainer getTrainer(String username) {
    System.out.println("map: "+trainerHashMap);
    return trainerHashMap.get(username);
  }

  public Stream<Trainer> getAllTrainers() {
    return trainerHashMap.values().stream();
  }
}
