package com.teamsync.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

import com.teamsync.api.features.auth.security.jwt.JwtProperties;

@SpringBootApplication
@EnableMongoAuditing
@EnableConfigurationProperties(JwtProperties.class)
public class TeamsyncApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TeamsyncApiApplication.class, args);
	}

}
