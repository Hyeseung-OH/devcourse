package com.back;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
// 스프링이 시작될 때
public class AppConfig {
//    @Bean
//    public PersonService personService() {
//        System.out.println("AppConfig.personService 호출됨");
//        return new PersonService();
//    }

    @Bean
    public PersonRepository personRepository() {
        return new PersonRepository(1);
    }

    @Bean
    public PersonRepository personRepositoryV2() {
        return new PersonRepository(2);
    }

}
