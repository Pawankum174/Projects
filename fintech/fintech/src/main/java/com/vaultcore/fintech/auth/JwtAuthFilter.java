package com.vaultcore.fintech.auth;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

private final JwtService jwt;

public JwtAuthFilter(JwtService jwt) {
 this.jwt = jwt;
}

@Override
protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
   throws ServletException, IOException {
 String auth = req.getHeader(HttpHeaders.AUTHORIZATION);
 if (auth != null && auth.startsWith("Bearer ")) {
   String token = auth.substring(7);
   try {
     String sub = jwt.extractSubject(token);
     String role = jwt.extractRole(token);
     
     UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
         sub, null, List.of(new SimpleGrantedAuthority("ROLE_" + role)));
     SecurityContextHolder.getContext().setAuthentication(authToken);
   } catch (Exception ignored) {
     SecurityContextHolder.clearContext();
   }
 }
 chain.doFilter(req, res);
}
}
