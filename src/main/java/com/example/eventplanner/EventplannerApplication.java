package com.example.eventplanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableScheduling
@SpringBootApplication
@EntityScan(basePackages = "com.example.eventplanner.model")
public class EventplannerApplication {

	public static void main(String[] args) {
		System.out.println("Hello");
		SpringApplication.run(EventplannerApplication.class, args);
	}

}
