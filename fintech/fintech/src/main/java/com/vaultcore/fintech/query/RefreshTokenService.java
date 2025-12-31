package com.vaultcore.fintech.query;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
* Simple in-memory refresh token store.
* For production, back this with a persistent store (DB/Redis) and rotation.
*/
@Service
public class RefreshTokenService {

public static class RefreshToken {
 public final String token;
 public final String userId;
 public final Instant expiry;

 public RefreshToken(String token, String userId, Instant expiry) {
   this.token = token; this.userId = userId; this.expiry = expiry;
 }
}

private final Map<String, RefreshToken> store = new ConcurrentHashMap<>();

public String issue(String userId, long ttlSeconds) {
 String token = UUID.randomUUID().toString();
 store.put(token, new RefreshToken(token, userId, Instant.now().plusSeconds(ttlSeconds)));
 return token;
}

public String validateAndConsume(String token) {
 RefreshToken rt = store.remove(token);
 if (rt == null || rt.expiry.isBefore(Instant.now())) return null;
 return rt.userId;
}
}
