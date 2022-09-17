package com.invicto.wui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;

@SpringBootApplication
public class WebUiApplication {

	public static void main(String[] args) {
		ApplicationContext co = SpringApplication.run(WebUiApplication.class, args);
	}

	@Bean
	public JdbcTemplate createJdbcTemplate(@Autowired DataSource dataSource){
		return new JdbcTemplate(dataSource);
	};

	@Bean
	public RestTemplate createRestTemplate(){
		return new RestTemplate();
	}
}
