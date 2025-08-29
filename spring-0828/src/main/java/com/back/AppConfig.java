package com.back;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;

@Configuration
// 스프링이 시작될 때
public class AppConfig {
    @Autowired
    @Lazy
    private AppConfig self;

    @Bean
    public ApplicationRunner myApplicationRunner() {
        return args -> {
            // 트랜잭션이 걸리지 않은 메서드를 호출할 때는 this로 호출해도 된다.
            this.work1();
            this.work2();

            // 트랜잭션이 걸린 메서드를 호출할 때는 프록시 객체를 통해서 호출해야 한다.
            self.work1();
            self.work2();
        };
    }

    @Transactional
    public void work1() {
        System.out.println("work1");
    }

    @Transactional
    public void work2() {
        System.out.println("work2");
    }
}
