package com.example.loja;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class LojaApplication {

	public static void main(String[] args) {

		SpringApplication.run(LojaApplication.class, args);

		Dotenv dotenv = Dotenv.load();

		System.setProperty("USER_EMAIL", dotenv.get("USER_EMAIL"));
		System.setProperty("PASSWORD_USER", dotenv.get("PASSWORD_USER"));
	}

}
