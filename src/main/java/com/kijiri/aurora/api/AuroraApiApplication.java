package com.kijiri.aurora.api;

import com.kijiri.aurora.api.model.Role;
import com.kijiri.aurora.api.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AuroraApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuroraApiApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner commandLineRunner(RoleRepository roleRepository) {
		return args -> {
			if (roleRepository.findByName("USER").isEmpty()) {
				roleRepository.save(Role.builder().name("USER").build());
			}
			if (roleRepository.findByName("ADMIN").isEmpty()) {
				roleRepository.save(Role.builder().name("ADMIN").build());
			}
		};
	}

}
