Feature: Authentication

  Scenario: Successful login
    Given a user with username "username" and password "password" exists
    When a login request with username "username" and password "password" is sent
    Then the response status should be 200

  Scenario: Invalid credentials
    Given a user with username "username" and password "password" does not exist
    When a login request with username "username" and password "password" is sent
    Then the response status should be 401