package org.example.websitetechworld;

import org.example.websitetechworld.Config.EnvLoader;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication(
        scanBasePackages = "org.example.websitetechworld"
)
@EnableJpaRepositories("org.example.websitetechworld.Repository")
@EntityScan("org.example.websitetechworld.Entity")
public class WebsiteTechWorldApplication implements CommandLineRunner {

    public static void main(String[] args) {
        new EnvLoader();
        SpringApplication.run(WebsiteTechWorldApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode("password123");  // Mã hóa mật khẩu

        System.out.println(encodedPassword);  // In ra mật khẩu đã mã hóa để dùng cho SQL
    }
}
