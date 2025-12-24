package com.vaultcore.fintech.auth;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

private final Key key;
private final String issuer;
private final long accessTtlSeconds;

public JwtService(
   @Value("${vaultcore.jwt.secret}") String secret,
   @Value("${vaultcore.jwt.issuer}") String issuer,
   @Value("${vaultcore.jwt.access-ttl-seconds}") long accessTtlSeconds) {
 this.key = Keys.hmacShaKeyFor(secret.getBytes());
 this.issuer = issuer;
 this.accessTtlSeconds = accessTtlSeconds;
}

public String issueAccessToken(String subject, String role) {
 Instant now = Instant.now();
 Instant exp = now.plusSeconds(accessTtlSeconds);
 return Jwts.builder()
     .setSubject(subject)
     .setIssuer(issuer)
     .setIssuedAt(Date.from(now))
     .setExpiration(Date.from(exp))
     .addClaims(Map.of("role", role))
     .signWith(key, SignatureAlgorithm.HS256)
     .compact();
}

public String extractSubject(String token) {
 return Jwts.parserBuilder().setSigningKey(key).build()
     .parseClaimsJws(token).getBody().getSubject();
}

public String extractRole(String token) {
 Object role = Jwts.parserBuilder().setSigningKey(key).build()
     .parseClaimsJws(token).getBody().get("role");
 return role == null ? "USER" : role.toString();
}
}
