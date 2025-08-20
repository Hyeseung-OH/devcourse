package com.back.global.initData;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BaseInitData {
    @Bean
    ApplicationRunner InitDataRunner() {
        return args -> {
            System.out.println("Initializing base data...");
        };
    }
}
