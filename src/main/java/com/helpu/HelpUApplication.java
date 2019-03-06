package com.helpu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@ComponentScan
@EnableAutoConfiguration
public class HelpUApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelpUApplication.class, args);
	}

}
