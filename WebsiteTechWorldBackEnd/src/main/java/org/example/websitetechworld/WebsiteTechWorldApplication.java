package org.example.websitetechworld;

import org.example.websitetechworld.Config.EnvLoader;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class WebsiteTechWorldApplication implements CommandLineRunner {

    public static void main(String[] args) {
        new EnvLoader();
        SpringApplication.run(WebsiteTechWorldApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode("password123");

        System.out.println(encodedPassword);
    }
}
