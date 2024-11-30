package com.example.eventplanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.example.eventplanner.model")
public class EventplannerApplication {

	public static void main(String[] args) {
		System.out.println("Hello");
		SpringApplication.run(EventplannerApplication.class, args);
	}

}
