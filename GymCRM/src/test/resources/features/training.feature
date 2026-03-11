Feature: Training management

  Scenario: Create training successfully
    Given the training service is running
    When a valid training request is sent
    Then the response status should be 201

  Scenario: Create training with missing duration
    Given the training service is running
    When an invalid training request is sent
    Then the response status should be 400
