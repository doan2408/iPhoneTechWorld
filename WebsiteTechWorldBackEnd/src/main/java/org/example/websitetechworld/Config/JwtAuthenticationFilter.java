package org.example.websitetechworld.Config;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Services.LoginServices.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService ongjwtService;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String requestPath = request.getRequestURI();
        System.out.println("Request Path: " + requestPath);

        boolean requireAuth = requestPath.startsWith("/client/") ||
                requestPath.startsWith("/admin/") ||
                requestPath.startsWith("/api/auth/me");

        if (!requireAuth) {
            // Không cần xác thực → đi thẳng
            System.out.println("Bỏ qua xác thực cho: " + requestPath);
            filterChain.doFilter(request, response);
            return;
        }

//        if (requestPath.startsWith("/api/auth/") && !requestPath.equals("/api/auth/me")) {
//            System.out.println("Bỏ qua xác thực cho: " + requestPath);
//            filterChain.doFilter(request, response);
//            return;
//        }


        String authHeader = request.getHeader("Authorization");
        System.out.println("Authorization Header: " + authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("Không tìm thấy token trong header");
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "Thiếu token xác thực");
            return;
        }

        String jwt = authHeader.substring(7);
        try {
            String username = jwtService.extractUsername(jwt);
            System.out.println("Username từ token: " + username);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities()
                            );
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    System.out.println("Xác thực thành công cho: " + username);
                } else {
                    System.out.println("Token không hợp lệ hoặc hết hạn");
                    sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "Token không hợp lệ");
                    return;
                }
            }
            filterChain.doFilter(request, response);
        } catch (SignatureException e) {
            System.err.println("Lỗi xác minh chữ ký: " + e.getMessage());
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "Token không hợp lệ", e.getMessage());
        } catch (ExpiredJwtException e) {
            System.err.println("Token hết hạn: " + e.getMessage());
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "Token hết hạn", e.getMessage());
        } catch (Exception e) {
            System.err.println("Lỗi khác: " + e.getMessage());
            sendErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi server", e.getMessage());
        }
    }

    // ✅ Helper method để tránh lỗi "getOutputStream() has already been called"
    private void sendErrorResponse(HttpServletResponse response, HttpStatus status, String error) throws IOException {
        sendErrorResponse(response, status, error, null);
    }

    private void sendErrorResponse(HttpServletResponse response, HttpStatus status, String error, String message) throws IOException {
        if (response.isCommitted()) {
            System.err.println("Response đã được commit, không thể ghi thêm");
            return;
        }

        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String jsonResponse;
        if (message != null) {
            jsonResponse = String.format("{\"error\": \"%s\", \"message\": \"%s\"}", error, message);
        } else {
            jsonResponse = String.format("{\"error\": \"%s\"}", error);
        }

        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }
}

