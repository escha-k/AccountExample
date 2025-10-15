package com.example.account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AccountApplication {

	public static void main(String[] args) {
		System.out.println("ci 테스트를 위한 코드 추가");

		SpringApplication.run(AccountApplication.class, args);
	}

}
