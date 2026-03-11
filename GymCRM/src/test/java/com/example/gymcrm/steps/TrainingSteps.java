package com.example.gymcrm.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
public class TrainingSteps {

    private static final Logger log = LoggerFactory.getLogger(TrainingSteps.class);
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ScenarioContext context;

    @Given("the training service is running")
    public void serviceRunning() {
    }

    @When("a valid training request is sent")
    public void sendValidTraining() throws Exception {
        context.setResponse(mockMvc.perform(post("/trainings")
                        .param("traineeUsername", "trainee")
                        .param("trainerUsername", "trainer")
                        .param("trainingName", "trainer")
                        .param("trainingDate", "2025-06-05T11:00")
                        .param("trainingDuration", "1.5")));
    }

    @When("an invalid training request is sent")
    public void sendInvalidTraining() throws Exception {
        context.setResponse(mockMvc.perform(post("/trainings")
                        .param("traineeUsername", "trainee")
                        .param("trainerUsername", "trainer")
                        .param("trainingName", "trainer")
                        .param("trainingDate", "2025-06-05T11:00")));
    }
}