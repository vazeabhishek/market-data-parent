package com.invicto.fbl;

import com.invicto.fbl.helper.MathsHelper;
import com.invicto.fbl.service.BhavCopyReader;
import com.invicto.fbl.service.BhavCopyRecordProcessor;
import com.invicto.fbl.service.calc.Calculator;
import com.invicto.fbl.service.calc.impl.OiCalculator;
import com.invicto.fbl.service.calc.impl.PriceCalculator;
import com.invicto.fbl.service.calc.impl.VolumeCalculator;
import com.invicto.fbl.service.chain.Processor;
import com.invicto.fbl.service.chain.impl.*;
import com.invicto.mdp.entity.ProcessLog;
import com.invicto.mdp.repository.ProcessLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.File;
import java.time.LocalDate;

@SpringBootApplication(scanBasePackages = {"com.invicto.mdp", "com.invicto.fbl"})
@EnableJpaRepositories(value = "com.invicto.mdp.repository")
@EntityScan("com.invicto.mdp.entity")
@Slf4j
public class BhavLoaderApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(BhavLoaderApplication.class, args);
        ProcessLogRepository logRepository = context.getBean(ProcessLogRepository.class);

        if (args.length > 0) {
            String pathToFile = args[0];
            File file = new File(pathToFile);
            if (file.exists() && logRepository.findByFileName(file.getName()).isEmpty()) {
                log.info("Starting processing file {}", pathToFile);
                BhavCopyReader bhavCopyReader = context.getBean(BhavCopyReader.class);
                BhavCopyRecordProcessor processor = context.getBean(BhavCopyRecordProcessor.class);
                bhavCopyReader.loadBhavCopy(file, processor);
                logRepository.save(processLog(file.getName()));
            } else {
                log.info("File not found {}", pathToFile);
                System.exit(-1);
            }
        } else {
            log.info("No files to process");
            System.exit(-1);
        }
    }

    private static ProcessLog processLog(String fileName) {
        ProcessLog processLog = new ProcessLog();
        processLog.setLocalDate(LocalDate.now());
        processLog.setFileName(fileName);
        return processLog;
    }


	@Bean
	public  MathsHelper mathsHelper(){
		return new MathsHelper();
	}
	@Bean("oiCalc")
	public Calculator oiCalculator(MathsHelper mathsHelper){
		return new OiCalculator(mathsHelper);
	}

	@Bean("volCalc")
	public Calculator volCalculator(MathsHelper mathsHelper){
		return new VolumeCalculator(mathsHelper);
	}

	@Bean("priceCalc")
	public Calculator priceCalculator(MathsHelper mathsHelper){
		return new PriceCalculator(mathsHelper);
	}

    @Bean("signalProcessor")
    public Processor signalProcessor() {
        return new SignalsProcessor();
    }

    @Bean("wickProcessor")
    public Processor WickProcessor(@Qualifier("signalProcessor") Processor processor, MathsHelper mathsHelper) {
        return new WickProcessor(processor, mathsHelper);
    }

    @Bean("trendProcessor")
    public Processor trendProcessor(@Qualifier("wickProcessor") Processor processor) {
        return new TrendProcessor(processor);
    }

    @Bean("volProcessor")
    public Processor volProcessor(@Qualifier("trendProcessor") Processor processor, @Qualifier("volCalc") Calculator calculator) {
        return new VolProcessor(processor, calculator);
    }

    @Bean("priceProcessor")
    public Processor priceProcessor(@Qualifier("volProcessor") Processor processor,@Qualifier("priceCalc") Calculator calculator) {
        return new PriceProcessor(processor, calculator);
    }

    @Bean("oiProcessor")
    public Processor oiProcessor(@Qualifier("priceProcessor") Processor processor, @Qualifier("oiCalc") Calculator calculator) {
        return new OiProcessor(processor, calculator);
    }
}
