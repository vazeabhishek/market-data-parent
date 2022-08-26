package com.invicto.ic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.invicto.mdp","com.invicto.ic"})
@EnableJpaRepositories(value = "com.invicto.mdp.repository")
public class IntradayCollectorApplication {

    public static void main(String[] args) {
        SpringApplication.run(IntradayCollectorApplication.class, args);
    }
}
