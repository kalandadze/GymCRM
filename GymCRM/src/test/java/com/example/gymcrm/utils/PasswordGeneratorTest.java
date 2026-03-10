package com.example.gymcrm.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordGeneratorTest {
  @Test
  void generatePasswordIsCorrectLength() {
    assertEquals(6, PasswordGenerator.generatePassword(6).length());
  }

  @Test
  void generatePasswordIsAllLetters() {
    String password = PasswordGenerator.generatePassword(20);
    assertTrue(password.matches("^[A-Za-z0-9]+$"));
  }

  @Test
  void passwordsShouldBeRandom() {
    String p1 = PasswordGenerator.generatePassword(10);
    String p2 = PasswordGenerator.generatePassword(10);
    assertNotEquals(p1, p2, "Passwords should not be identical across calls");
  }
}