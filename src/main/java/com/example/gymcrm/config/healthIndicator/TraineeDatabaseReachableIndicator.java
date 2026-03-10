package com.example.gymcrm.config.healthIndicator;

import com.example.gymcrm.repository.TraineeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;

@Component
public class TraineeDatabaseReachableIndicator extends AbstractHealthIndicator {
  @Autowired
  TraineeRepository traineeRepository;

  @Override
  protected void doHealthCheck(Health.Builder builder) {
    try {
      traineeRepository.getAll();
      builder.up().withDetail("training database", "database is reachable");
    } catch (Exception e) {
      builder.down().withDetail("training database", "database is not reachable: " + e.getMessage());
    }
  }
}