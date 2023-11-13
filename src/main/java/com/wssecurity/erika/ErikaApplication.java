package com.wssecurity.erika;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.wssecurity.erika.service.Generator;

@SpringBootApplication
public class ErikaApplication {

	@Autowired
	private Generator generatorService;

	public static void main(String[] args) {
		SpringApplication.run(ErikaApplication.class, args);
	}

	@Bean
	public CommandLineRunner run() throws Exception {
		return (String[] args) -> {
			generatorService.generateRoles();
			generatorService.generateUserList();
		};
	}

}
