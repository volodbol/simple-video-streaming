package com.volod.streaming;

import org.springframework.boot.SpringApplication;

public class TestSimpleVideoStreamingApplication {

	public static void main(String[] args) {
		SpringApplication.from(SimpleVideoStreamingApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
