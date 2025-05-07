package org.example.websitetechworld.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain authorizeFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests(req ->{
            req.requestMatchers("/admin/**").hasRole("ADMIN", "STAFF");
            req.requestMatchers("/client/**").hasRole("USER");
        }
        })
        return http.build();
    }


//    public UserDetailsService authentication(){
//
//    }
}
