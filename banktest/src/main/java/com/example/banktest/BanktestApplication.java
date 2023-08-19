package com.example.banktest;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Bank Test Api"))
public class BanktestApplication {

	public static void main(String[] args) {
		SpringApplication.run(BanktestApplication.class, args);
		System.out.println("This is a test");
	}

}
