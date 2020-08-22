package com.ag04.sbss.hackathon.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HackathonAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(HackathonAppApplication.class, args);
	}

}
