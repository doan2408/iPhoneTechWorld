package org.example.websitetechworld;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class WebsiteTechWorldApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(WebsiteTechWorldApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode("password456");  // Mã hóa mật khẩu

        System.out.println(encodedPassword);  // In ra mật khẩu đã mã hóa để dùng cho SQL
    }
}
