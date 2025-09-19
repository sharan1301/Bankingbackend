//package com.example.BankingBackend.utils;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.List;
//
//@Component
//public class JwtFilter extends OncePerRequestFilter {
//    @Autowired
//    JwtUtil jwtUtil;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        String authHeader = request.getHeader("Authorization");
//
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            String token = authHeader.substring(7);
//            if (jwtUtil.validateJwtToken(token)) {
//                String email = jwtUtil.extractEmail(token);
//                String role = jwtUtil.extractRole(token); // 🔹 New: extract role from token
//
//                var auth = new UsernamePasswordAuthenticationToken(
//                        email,
//                        null,
//                        List.of(new SimpleGrantedAuthority(role)) // 🔹 attach role
//                );
//
//                SecurityContextHolder.getContext().setAuthentication(auth);
//            }
//        }
//        filterChain.doFilter(request, response);
//    }
//}
