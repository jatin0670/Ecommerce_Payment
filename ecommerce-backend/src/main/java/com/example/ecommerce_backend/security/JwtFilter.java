package com.example.ecommerce_backend.security;

import com.example.ecommerce_backend.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {


    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        if(header != null && header.startsWith("Bearer ")){
            String token = header.substring(7);

            try {
                String email = jwtUtil.extractEmail(token);
                var user = userRepository.findByEmail(email).orElse(null);


                if(user != null){
                    var auth = new UsernamePasswordAuthenticationToken(user , null, user.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(auth);
                }

            } catch (Exception e){

            }
        }

        filterChain.doFilter(request, response);
    }
}
