    package org.example.websitetechworld.Config;


    import jakarta.servlet.http.HttpServletResponse;
    import org.example.websitetechworld.Services.LoginServices.AccountDetailService;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.config.Customizer;
    import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
    import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.http.SessionCreationPolicy;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.security.web.SecurityFilterChain;
    import org.springframework.web.cors.CorsConfiguration;
    import org.springframework.web.cors.CorsConfigurationSource;
    import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

    import java.util.List;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final AccountDetailService accountDetailService;

    public SecurityConfig(AccountDetailService accountDetailService) {
        this.accountDetailService = accountDetailService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors(Customizer.withDefaults()) // ✅ Kích hoạt CORS dùng config bên dưới
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/login", "/api/auth/register", "/css/**", "/js/**").permitAll()
                        .requestMatchers("/admin/**").hasAnyRole("ADMIN", "STAFF")
//                        .requestMatchers("/client/**").hasAnyRole("ADMIN", "STAFF", "KHACH_HANG")
                        .anyRequest().permitAll()
                )
                .formLogin().disable()        // ❌ Tắt login mặc định bằng form
                .httpBasic().disable()        // ❌ Tắt Basic Auth
                .logout(logout -> logout      // ✅ Logout thủ công nếu cần
                        .logoutUrl("/api/auth/logout")
                        .deleteCookies("JSESSIONID") // 👈 giúp frontend sạch session
                        .logoutSuccessHandler((req, res, auth) -> res.setStatus(HttpServletResponse.SC_OK))
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // Dùng session khi cần
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http,
                                                       PasswordEncoder passwordEncoder) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(accountDetailService)
                .passwordEncoder(passwordEncoder)
                .and()
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedMethod("*"); // Cho phép mọi phương thức HTTP
        configuration.addAllowedHeader("*"); // Cho phép mọi header
        configuration.addAllowedOrigin("http://localhost:5173"); // Chỉ cho phép frontend từ cổng 5173
//        configuration.addAllowedOriginPattern("*"); // KHÔNG nên dùng cùng lúc với addAllowedOrigin()
        configuration.setAllowCredentials(true); // Cho phép gửi cookie (nếu cần)

        // Đăng ký cấu hình CORS cho tất cả các đường dẫn
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

//    Lưu ý: KHÔNG dùng addAllowedOrigin("*") khi có setAllowCredentials(true)
//    — điều này sẽ gây lỗi CORS silently.

}


