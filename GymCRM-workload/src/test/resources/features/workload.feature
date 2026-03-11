Feature: Trainer workload management

  Scenario: Create trainer workload successfully
    Given a trainer workload request with trainer "trainer1" and duration 60
    When the workload request is processed
    Then the workload for trainer "trainer1" should be 60

  Scenario: Update trainer workload
    Given a trainer "trainer1" already has workload 60
    And a trainer workload request with trainer "trainer1" and duration 30
    When the workload request is processed
    Then the workload for trainer "trainer1" should be 90

  Scenario: Invalid workload request
    Given an invalid workload request
    When the workload request is processed
    Then the workload request should fail