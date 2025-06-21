package com.example.runity;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(servers = {@Server(url = "/", description = "Default Server URL")}) // 추가
public class RunityApplication {

	public static void main(String[] args) {
		SpringApplication.run(RunityApplication.class, args);
	}

}
//push test