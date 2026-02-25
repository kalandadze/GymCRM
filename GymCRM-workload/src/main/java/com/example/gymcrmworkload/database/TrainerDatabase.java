package com.example.gymcrmworkload.database;

import com.example.gymcrmworkload.model.TrainerWorkload;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
@Component
public class TrainerDatabase {
    private final List<TrainerWorkload> trainers = new ArrayList<>();
}
