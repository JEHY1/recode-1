package com.example.recode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class RecodeApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecodeApplication.class, args);
	}

}
