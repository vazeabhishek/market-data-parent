package com.invicto.fbl;

import com.invicto.fbl.service.BhavCopyReader;
import com.invicto.fbl.service.BhavCopyRecordProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.File;

@SpringBootApplication(scanBasePackages = {"com.invicto.mdp","com.invicto.fbl"})
@EnableJpaRepositories(value = "com.invicto.mdp.repository")
@EntityScan("com.invicto.mdp.entity")
@Slf4j
public class BhavLoaderApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(BhavLoaderApplication.class, args);

		if(args.length > 0){
			String pathToFile = args[0];
			File file = new File(pathToFile);
			if(file.exists()){
				log.info("Starting processing file {}",pathToFile);
				BhavCopyReader bhavCopyReader = context.getBean(BhavCopyReader.class);
				BhavCopyRecordProcessor processor = context.getBean(BhavCopyRecordProcessor.class);
				bhavCopyReader.loadBhavCopy(file,processor);
			}
			else {
				log.info("File not found {}",pathToFile);
				System.exit(-1);
			}
		}
		else {
			log.info("No files to process");
			System.exit(-1);
		}
	}
}
