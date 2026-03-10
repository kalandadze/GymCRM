package com.example.gymcrm.config;

import com.example.gymcrm.utils.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {
  private final JwtUtils jwtUtils;
  private final UserDetailsService userDetailsService;

  public JwtAuthorizationFilter(JwtUtils jwtUtils, UserDetailsService userDetailsService) {
    this.jwtUtils = jwtUtils;
    this.userDetailsService = userDetailsService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
    String token = request.getHeader(jwtUtils.JWT_HEADER);
    if (token == null || !token.startsWith(jwtUtils.JWT_PREFIX)) {
      chain.doFilter(request, response);
      return;
    }
    UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(token);
    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    chain.doFilter(request, response);
  }

  private UsernamePasswordAuthenticationToken getAuthentication(String token) {
    token = token.replace(jwtUtils.JWT_PREFIX, "");
    try {
      String username = jwtUtils.getUsernameFromToken(token);
      if (jwtUtils.validateToken(token, username)) {
        return new UsernamePasswordAuthenticationToken(userDetailsService.loadUserByUsername(username), null, List.of());
      }
    } catch (ExpiredJwtException e) {
    }
    return null;
  }
}
