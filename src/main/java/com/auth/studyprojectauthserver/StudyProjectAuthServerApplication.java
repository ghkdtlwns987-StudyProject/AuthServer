package com.auth.studyprojectauthserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class StudyProjectAuthServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudyProjectAuthServerApplication.class, args);
	}
}
