package com.example.gymcrm.controller;

import com.example.gymcrm.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Operations for logging in the application")
@Slf4j
public class AuthenticationController {
    UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    @Operation(summary = "log in the application")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully logged in"),
            @ApiResponse(responseCode = "401", description = "Your username or password is incorrect"),
            @ApiResponse(responseCode = "403", description = "You are blocked from trying to log in for 5 minutes"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "422", description = "Application failed due to receiving invalid field in the request"),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    }
    )
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
        try {
            String token = userService.login(username, password);
            return ResponseEntity.ok().body(token);
        } catch (NoSuchElementException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(401).body(e.getMessage());
        } catch (AccessDeniedException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(500).body("Application failed to process the request");
        }
    }

    @PutMapping("/login/change")
    @Operation(summary = "log in the application")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully logged in"),
            @ApiResponse(responseCode = "401", description = "Your username or password is incorrect"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "422", description = "Application failed due to receiving invalid field in the request"),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request")
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
