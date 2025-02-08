////package com.example.Auth.Config;
////
////import jakarta.servlet.FilterChain;
////import jakarta.servlet.ServletException;
////import jakarta.servlet.http.HttpServletRequest;
////import jakarta.servlet.http.HttpServletResponse;
////import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
////import org.springframework.security.core.context.SecurityContextHolder;
////import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
////import org.springframework.web.filter.OncePerRequestFilter;
////
////import java.io.IOException;
////
////public class JwtAuthenticationFilter extends OncePerRequestFilter {
////
////    private final JwtUtil jwtUtil;
////
////    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
////        this.jwtUtil = jwtUtil;
////    }
////
////    @Override
////    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
////            throws ServletException, IOException {
////
////        String header = request.getHeader("Authorization");
////
////        if (header != null && header.startsWith("Bearer ")) {
////            String token = header.substring(7);
////
////            if (jwtUtil.isTokenExpired(token)) {
////                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is expired");
////                return;
////            }
////
////            String username = jwtUtil.extractUsername(token);
////
////            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
////                if (jwtUtil.validateToken(token, username)) {
////                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
////                            username, null, null);
////                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
////                    SecurityContextHolder.getContext().setAuthentication(authentication);
////                }
////            }
////        }
////
////        chain.doFilter(request, response);
////    }
////}
//
//
//package com.example.Auth.Config;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.web.filter.OncePerRequestFilter;
//import io.jsonwebtoken.SignatureException;
//
//import java.io.IOException;
//
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//    private final JwtUtil jwtUtil;
//
//    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
//        this.jwtUtil = jwtUtil;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
//            throws ServletException, IOException {
//
//        String header = request.getHeader("Authorization");
//
//        if (header != null && header.startsWith("Bearer ")) {
//            String token = header.substring(7);
//
//            try {
//                if (jwtUtil.isTokenExpired(token)) {
//                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is expired");
//                    return;
//                }
//
//                String username = jwtUtil.extractUsername(token);
//
//                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//                    if (jwtUtil.validateToken(token, username)) {
//                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
//                                username, null, null);
//                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                        SecurityContextHolder.getContext().setAuthentication(authentication);
//                    }
//                }
//            } catch (SignatureException e) {
//                // Handle JWT signature error and return a custom error response
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                response.setContentType("application/json");
//                response.getWriter().write("{\"error\": \"JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted.\"}");
//                return;
//            }
//        }
//
//        chain.doFilter(request, response);
//    }
//}


package com.example.Auth.Config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            try {
                if (jwtUtil.isTokenExpired(token)) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is expired");
                    return;
                }

                String username = jwtUtil.extractUsername(token);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    // Validate token
                    if (jwtUtil.validateToken(token, username)) {
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                username, null, null);
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            } catch (SignatureException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");

                Map<String, Object> errorResponse = new LinkedHashMap<>();
                errorResponse.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                errorResponse.put("status", HttpServletResponse.SC_UNAUTHORIZED);
                errorResponse.put("error", "Unauthorized");
                errorResponse.put("message", "JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted.");

                ObjectMapper objectMapper = new ObjectMapper();
                response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
                return;
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json");

                Map<String, Object> errorResponse = new LinkedHashMap<>();
                errorResponse.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                errorResponse.put("status", HttpServletResponse.SC_FORBIDDEN);
                errorResponse.put("error", "Forbidden");
                errorResponse.put("message", "Access is forbidden. You do not have permission to access this resource.");

                ObjectMapper objectMapper = new ObjectMapper();
                response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
                return;
            }

        }

        chain.doFilter(request, response);
    }
}
