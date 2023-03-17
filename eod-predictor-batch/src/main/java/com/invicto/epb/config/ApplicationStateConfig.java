package com.invicto.epb.config;

import com.invicto.epb.service.bridge.ContractService;
import com.invicto.epb.service.bridge.EquityService;
import com.invicto.epb.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationStateConfig {

    @Bean
    public FinalProcessor finalProcessor(@Autowired EquityService equityService){
        return new FinalProcessor(null,equityService);
    }
    @Bean
    public ContractFutureProcessor contractFutureProcessor(@Autowired ContractService contractService,@Autowired FinalProcessor finalProcessor){
        return new ContractFutureProcessor(finalProcessor,contractService);
    }

    @Bean
    public ContractHistoryProcessor contractHistoryProcessor(@Autowired ContractFutureProcessor contractFutureProcessor, @Autowired ContractService contractService){
        return new ContractHistoryProcessor(contractFutureProcessor,contractService);
    }

    @Bean
    public PriceProcessor priceProcessor(@Autowired ContractHistoryProcessor contractHistoryProcessor, @Autowired EquityService equityService){
        return new PriceProcessor(contractHistoryProcessor,equityService);
    }

    @Bean
    public OIProcessor oiProcessor(@Autowired PriceProcessor processor){
        return new OIProcessor(processor);
    }

}
