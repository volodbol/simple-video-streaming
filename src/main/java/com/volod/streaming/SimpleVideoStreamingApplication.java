package com.volod.streaming;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
		info = @Info(
				title = "${spring.application.name}"
		)
)
@SpringBootApplication
public class SimpleVideoStreamingApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleVideoStreamingApplication.class, args);
	}

}
