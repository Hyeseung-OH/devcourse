package com.back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Spring0828Application {

    public static void main(String[] args) {
        SpringApplication.run(Spring0828Application.class, args);
    }

}
