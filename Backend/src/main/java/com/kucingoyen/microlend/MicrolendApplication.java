package com.kucingoyen.microlend;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MicrolendApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicrolendApplication.class, args);
	}
	@PostConstruct
	public void verify() {
		System.out.println("DB URL = " + System.getenv("spring.datasource.url"));
	}

}
