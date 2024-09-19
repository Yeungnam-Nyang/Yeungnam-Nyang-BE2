package com.example.YNN;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class YnnApplication {

	public static void main(String[] args) {
		SpringApplication.run(YnnApplication.class, args);
	}

}
