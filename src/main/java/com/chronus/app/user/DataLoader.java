package com.chronus.app.user;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDatabase(UserRepository repository, UserRepository userRepository) {
        return args -> {
            userRepository.save(new User("Flaco López", "password", "flakitomatador@sep.com"));
            userRepository.save(new User("Vitor Roque", "password", "vitinbates@sep.com"));
            System.out.println("Created table and inserted users with success!");
        };
    }
}
