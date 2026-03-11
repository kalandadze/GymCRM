package com.example.gymcrmworkload.steps;

import com.example.gymcrmworkload.repository.TrainerWorkloadRepository;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@CucumberContextConfiguration
@SpringBootTest
@AutoConfigureMockMvc
class GymCrmApplicationTests {

    @MockitoBean
    private TrainerWorkloadRepository repository;
}
