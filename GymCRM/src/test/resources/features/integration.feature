Feature: Trainer workload integration

  Scenario: Training creation creates a trainer workload
    Given a training for trainer "trainer"
    When the training is processed
    Then the trainer with username "trainer" workload exists

  Scenario: Invalid training request
    Given an invalid training request
    When the training is processed
    Then the request should fail