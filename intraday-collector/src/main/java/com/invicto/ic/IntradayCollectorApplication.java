package com.invicto.ic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.invicto.ic.bridge.NseBridge;
import com.invicto.ic.model.SnapType;
import com.invicto.ic.service.IntraDataCollector;
import com.invicto.ic.service.IntradayDataPersister;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@SpringBootApplication(scanBasePackages = {"com.invicto.mdp","com.invicto.ic"})
@EnableJpaRepositories(value = "com.invicto.mdp.repository")
@EntityScan("com.invicto.mdp.entity")
@Slf4j
public class IntradayCollectorApplication {

    public static void main(String[] args) {
        Long start = System.currentTimeMillis();
        ApplicationContext ctx = SpringApplication.run(IntradayCollectorApplication.class, args);
        IntraDataCollector collector1h = ctx.getBean("collector1h",IntraDataCollector.class);
        IntraDataCollector collector15m = ctx.getBean("collector15m",IntraDataCollector.class);
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(2);
        executorService.scheduleAtFixedRate(collector15m, 0L, 15, TimeUnit.MINUTES);
        executorService.scheduleAtFixedRate(collector1h, 0L, 60, TimeUnit.MINUTES);
        Long end = System.currentTimeMillis();
        log.info("Application started in "+((end - start)/1000)+" Seconds");
    }

    @Bean
    public ObjectMapper objectMapperBean(){
        return new ObjectMapper();
    }

    @Bean("collector15m")
    public IntraDataCollector intraDataCollector15m(@Autowired NseBridge nseBridge, @Autowired IntradayDataPersister dataPersister){
        return new IntraDataCollector(nseBridge,dataPersister,SnapType.M15);
    }

    @Bean("collector1h")
    public IntraDataCollector intraDataCollector1h(@Autowired NseBridge nseBridge, @Autowired IntradayDataPersister dataPersister){
        return new IntraDataCollector(nseBridge,dataPersister,SnapType.H1);
    }

    @Bean(name = "longDateFormatterBean")
    public DateTimeFormatter createLongDateFormatter() {
        return new DateTimeFormatterBuilder().parseCaseInsensitive()
                .appendPattern("dd-MMM-yyyy HH:mm:ss")
                .toFormatter(Locale.ENGLISH);
    }
}
