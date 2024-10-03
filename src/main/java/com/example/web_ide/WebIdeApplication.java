package com.example.web_ide;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class WebIdeApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebIdeApplication.class, args);
	}

}
