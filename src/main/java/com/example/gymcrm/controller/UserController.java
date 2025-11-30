package com.example.gymcrm.controller;

import com.example.gymcrm.model.User;
import com.example.gymcrm.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/user")
@Api(produces = "application/json", value = "Operations for logging in the application")
@Slf4j
public class UserController {
  UserService userService;

  public UserController(UserService userService) {
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
  public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
    try {
      User user = userService.getUserByUsernameAndPassword(username, password);
      ResponseCookie usernameCookie = encodedCookie("username", user.getUsername());
      ResponseCookie passwordCookie = encodedCookie("password", user.getPassword());
      return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, usernameCookie.toString()).header(HttpHeaders.SET_COOKIE, passwordCookie.toString()).build();
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
  public ResponseEntity<?> changeLogin(@CookieValue("username") String username, @CookieValue("password") String password, @RequestParam String newPassword) {
    username=decodeCookie(username);
    password=decodeCookie(password);
    try {
      userService.changePassword(username, password, newPassword);
      ResponseCookie passwordCookie = encodedCookie("password", newPassword);
      return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, passwordCookie.toString()).build();
    } catch (NoSuchElementException e) {
      log.error(e.getMessage());
      return ResponseEntity.status(401).body(e.getMessage());
    } catch (Exception e) {
      log.error(e.getMessage());
      return ResponseEntity.status(500).body("Application failed to process the request");
    }
  }

  private ResponseCookie encodedCookie(String contentName, String contentValue) {
    return ResponseCookie.from(contentName, URLEncoder.encode(contentValue, StandardCharsets.UTF_8)).httpOnly(true).path("/").build();
  }

  private String decodeCookie(String encodedCookie) {
    return URLDecoder.decode(encodedCookie, StandardCharsets.UTF_8);
  }
}
