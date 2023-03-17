package com.invicto.epb.config;

import com.invicto.epb.service.ContractService;
import com.invicto.epb.service.EquityService;
import com.invicto.epb.service.Processor;
import com.invicto.epb.service.impl.ContractFutureProcessor;
import com.invicto.epb.service.impl.ContractHistoryProcessor;
import com.invicto.epb.service.impl.OIProcessor;
import com.invicto.epb.service.impl.PriceProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationStateConfig {

    @Bean
    public ContractFutureProcessor contractFutureProcessor(@Autowired ContractService contractService){
        return new ContractFutureProcessor(null,contractService);
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
