package com.invicto.epb;

import com.invicto.epb.service.SymbolEodPredictionOrch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.invicto.mdp","com.invicto.epb"})
@EnableJpaRepositories(value = "com.invicto.mdp.repository")
@EntityScan("com.invicto.mdp.entity")
@Slf4j
public class PredictorApplication {

    public static void main(String[] args) {
        Long start = System.currentTimeMillis();
        ApplicationContext ctx = SpringApplication.run(PredictorApplication.class, args);
        Long end = System.currentTimeMillis();
        SymbolEodPredictionOrch batch = ctx.getBean(SymbolEodPredictionOrch.class);
        batch.predictPricesForAllSymbols();
        log.info("Application started in "+((end - start)/1000)+" Seconds");

    }
}
