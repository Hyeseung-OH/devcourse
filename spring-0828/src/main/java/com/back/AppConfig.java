package com.back;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

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

    @Bean
    @Order(2)
    public ApplicationRunner myApplicationRunner1() {
        return new MyApplicationRunner(1);
    }

    @Bean
    @Order(1)
    public ApplicationRunner myApplicationRunner2() {
        return new MyApplicationRunner(2);
    }

//    @Bean
//    public ApplicationRunner myApplicationRunner3() {
//        return args -> {
//            work1();
//            work2();
//        };
//    }
//
//    public void work1() {
//        System.out.println("work1");
//    }
//
//    public void work2() {
//        System.out.println("work2");
//    }
}
