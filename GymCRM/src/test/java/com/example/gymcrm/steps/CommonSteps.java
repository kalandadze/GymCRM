package com.example.gymcrm.steps;

import io.cucumber.java.en.Then;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CommonSteps {

    @Autowired
    private ScenarioContext context;

    @Then("the response status should be {int}")
    public void responseStatusShouldBe(int status) throws Exception {
        context.getResponse().andExpect(status().is(status));
    }
}