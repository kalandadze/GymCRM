package com.example.gymcrm.controller;

import com.example.gymcrm.model.GymUser;
import com.example.gymcrm.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/auth")
@Api(produces = "application/json", value = "Operations for logging in the application")
@Slf4j
public class AuthenticationController {
  UserService userService;

  public AuthenticationController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/login")
  @ApiOperation(value = "log in the application")
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Successfully logged in"),
    @ApiResponse(code = 401, message = "Your username or password is incorrect"),
    @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
    @ApiResponse(code = 422, message = "Application failed due to receiving invalid field in the request"),
    @ApiResponse(code = 500, message = "Application failed to process the request")
  }
  )
  @Deprecated
  public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
    try {
      GymUser user = userService.getUserByUsernameAndPassword(username, password);
      return ResponseEntity.ok().build();
    } catch (NoSuchElementException e) {
      log.error(e.getMessage());
      return ResponseEntity.status(401).body(e.getMessage());
    } catch (Exception e) {
      log.error(e.getMessage());
      return ResponseEntity.status(500).body("Application failed to process the request");
    }
  }

  @PutMapping("/login/change")
  @ApiOperation(value = "log in the application")
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Successfully logged in"),
    @ApiResponse(code = 401, message = "Your username or password is incorrect"),
    @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
    @ApiResponse(code = 422, message = "Application failed due to receiving invalid field in the request"),
    @ApiResponse(code = 500, message = "Application failed to process the request")
  }
  )
  public ResponseEntity<?> changeLogin(@AuthenticationPrincipal UserDetails user, @RequestParam String newPassword) {
    try {
      userService.changePassword(user.getUsername(), newPassword);
      return ResponseEntity.ok().build();
    } catch (NoSuchElementException e) {
      log.error(e.getMessage());
      return ResponseEntity.status(401).body(e.getMessage());
    } catch (Exception e) {
      log.error(e.getMessage());
      return ResponseEntity.status(500).body("Application failed to process the request");
    }
  }
}
