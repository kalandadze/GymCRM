package com.example.gymcrmeureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class GymCrmEurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(GymCrmEurekaApplication.class, args);
    }

}
