package com.example.gymcrm.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {

  public static final long JWT_TOKEN_VALIDITY = 24 * 60 * 60 * 1000;
  public String JWT_HEADER = "Authorization";
  public String JWT_PREFIX = "Bearer ";
  public String JWT_SECRET = "ZmRkZjEwY2Y1ZDEyN2I4YzE2M2Q0ZjVlZjEyY2Y3NDY3ZTQ3YTgxZA==";

  public String generateToken(String username) {
    return Jwts.builder()
      .setSubject(username)
      .setIssuedAt(new Date())
      .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
      .signWith(SignatureAlgorithm.HS256, JWT_SECRET)
      .compact();
  }

  private Claims getAllClaimsFromToken(String token) {
    return Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody();
  }

  public String getUsernameFromToken(String token) {
    return getAllClaimsFromToken(token).getSubject();
  }

  public Date getExpirationDateFromToken(String token) {
    return getAllClaimsFromToken(token).getExpiration();
  }

  private Boolean isTokenExpired(String token) {
    return getExpirationDateFromToken(token).before(new Date());
  }

  public Boolean validateToken(String token, String username) {
    return getUsernameFromToken(token).equals(username) && !isTokenExpired(token);
  }

}
