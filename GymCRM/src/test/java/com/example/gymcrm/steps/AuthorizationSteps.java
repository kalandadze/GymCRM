package com.example.gymcrm.steps;

import com.example.gymcrm.model.GymUser;
import com.example.gymcrm.model.Trainee;
import com.example.gymcrm.repository.UserRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class AuthorizationSteps {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ScenarioContext context;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Given("a user with username {string} and password {string} exists")
    public void userExists(String username, String password) {
        GymUser user = new Trainee();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        Mockito.when(userRepository.findByUsernameAndPassword(username,password))
                .thenReturn(Optional.of(user));
    }

    @Given("a user with username {string} and password {string} does not exist")
    public void userDoesNotExists(String username, String password) {
        Mockito.when(userRepository.findByUsername(username))
                .thenReturn(Optional.empty());
    }

    @When("a login request with username {string} and password {string} is sent")
    public void loginRequest(String username, String password) throws Exception {
        context.setResponse(mockMvc.perform(
                get("/auth/login")
                        .param("username", username)
                        .param("password", password)));
    }
}
