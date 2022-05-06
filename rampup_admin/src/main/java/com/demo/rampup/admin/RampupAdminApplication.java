package com.demo.rampup.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@ComponentScan("com.demo.rampup")
@EnableAsync
@SpringBootApplication
public class RampupAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(RampupAdminApplication.class, args);
    }

}
