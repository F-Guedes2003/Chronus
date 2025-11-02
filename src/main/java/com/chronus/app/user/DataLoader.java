package com.chronus.app.user;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDatabase(UserRepository repository, UserRepository userRepository) {
        return args -> {
            System.out.println("Created table!");
        };
    }
}
