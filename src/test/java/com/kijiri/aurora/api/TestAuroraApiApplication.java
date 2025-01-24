package com.kijiri.aurora.api;

import org.springframework.boot.SpringApplication;

public class TestAuroraApiApplication {

	public static void main(String[] args) {
		SpringApplication.from(AuroraApiApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
