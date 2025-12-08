package com.service.student;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.service.student")
public class StudentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudentServiceApplication.class, args);
	}

}
