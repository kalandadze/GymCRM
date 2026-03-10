package com.example.gymcrm.model;

import lombok.Data;

@Data
public class LoginInfo {
  private String username;
  private String password;

  public LoginInfo(String username, String password) {
    this.username = username;
    this.password = password;
  }
}
