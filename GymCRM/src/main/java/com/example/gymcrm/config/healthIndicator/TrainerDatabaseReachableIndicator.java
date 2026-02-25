package com.example.gymcrm.config.healthIndicator;

import com.example.gymcrm.repository.TrainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;

@Component
public class TrainerDatabaseReachableIndicator extends AbstractHealthIndicator {
  @Autowired
  TrainerRepository trainerRepository;

  @Override
  protected void doHealthCheck(Health.Builder builder) {
    try {
      trainerRepository.getAll();
      builder.up().withDetail("trainer database", "database is reachable");
    } catch (Exception e) {
      builder.down().withDetail("trainer database", "database is not reachable: " + e.getMessage());
    }
  }
}