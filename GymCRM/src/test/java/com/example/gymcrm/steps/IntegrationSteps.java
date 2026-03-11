package com.example.gymcrm.steps;


import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationSteps {

    private ResultActions response;

    @Autowired
    private MockMvc mockMvc;

    private LocalDateTime trainingDate = LocalDateTime.now();
    private double trainingDuration;
    private String trainerUsername;
    private String traineeUsername;

    @Given("a training for trainer {string}")
    public void createTraining(String trainer) {
        trainerUsername = trainer;
        traineeUsername = "trainee";
        trainingDuration = 2.0;
    }

    @When("the training is processed")
    public void trainingProcessed() throws Exception {

        response = mockMvc.perform(post("/trainings")
                .param("traineeUsername", traineeUsername)
                .param("trainerUsername", trainerUsername)
                .param("trainingName", "yoga")
                .param("trainingDate", trainingDate.toString())
                .param("trainingDuration", trainingDuration + ""));
    }

    @Then("the trainer with username {string} workload exists")
    public void verifyWorkload(String username) throws Exception {
        response.andExpect(status().is2xxSuccessful());
    }

    @Given("an invalid training request")
    public void invalidRequest() {
        trainerUsername = null;
        traineeUsername = null;
        trainingDuration = 0.0;
    }

    @Then("the request should fail")
    public void requestShouldFail() throws Exception {
        response.andExpect(status().is4xxClientError());
    }
}
