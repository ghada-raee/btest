package com.example.banktest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BanktestApplication {

	public static void main(String[] args) {
		SpringApplication.run(BanktestApplication.class, args);
		System.out.println("This is a test");
	}

}
