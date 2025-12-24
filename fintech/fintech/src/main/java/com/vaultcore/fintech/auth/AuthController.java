package com.vaultcore.fintech.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.vaultcore.fintech.users.User;
import com.vaultcore.fintech.users.UserRepo;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final UserRepo users;
  private final PasswordEncoder encoder;
  private final JwtService jwt;
  private final RefreshTokenService refresh;
  private final PasswordResetTokenRepo resetRepo;
  private final EmailService email;

  public AuthController(UserRepo users, PasswordEncoder encoder, JwtService jwt,
                        RefreshTokenService refresh, PasswordResetTokenRepo resetRepo,
                        EmailService email) {
    this.users = users;
    this.encoder = encoder;
    this.jwt = jwt;
    this.refresh = refresh;
    this.resetRepo = resetRepo;
    this.email = email;
  }

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody Map<String,String> body) {
    String username = body.get("username");
    String pw = body.get("password");
    if (username == null || pw == null) {
      return ResponseEntity.badRequest().body(Map.of("error","missing"));
    }
    if (users.findByUsername(username).isPresent()) {
      return ResponseEntity.badRequest().body(Map.of("error","exists"));
    }
    User u = new User();
    u.setUsername(username);
    u.setPasswordHash(encoder.encode(pw));
    u.setRole("USER");
    users.save(u);
    return ResponseEntity.ok(Map.of("message","ok"));
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody Map<String,String> body) {
    String username = body.get("username");
    String pw = body.get("password");

    User u = users.findByUsername(username).orElse(null); // explicit type
    if (u == null || !encoder.matches(pw, u.getPasswordHash())) {
      return ResponseEntity.status(401).body(Map.of("error","bad creds"));
    }

    String access = jwt.issueAccessToken(u.getId().toString(), u.getRole());
    String rt = refresh.issue(u.getId().toString(), 7 * 24 * 3600); // 7 days
    return ResponseEntity.ok(Map.of("access_token", access, "refresh_token", rt));
  }

  @PostMapping("/token/refresh")
  public ResponseEntity<?> refresh(@RequestBody Map<String,String> body) {
    String rt = body.get("refresh_token");
    String userId = refresh.validateAndConsume(rt);
    if (userId == null) {
      return ResponseEntity.status(401).body(Map.of("error","invalid"));
    }

    User u = users.findById(UUID.fromString(userId)).orElseThrow(); // explicit type
    String access = jwt.issueAccessToken(userId, u.getRole());
    String newRt = refresh.issue(userId, 7 * 24 * 3600);
    return ResponseEntity.ok(Map.of("access_token", access, "refresh_token", newRt));
  }

  @PostMapping("/reset-password")
  public ResponseEntity<?> reset(@RequestBody Map<String,String> body) {
    String emailAddr = body.get("email"); // demo: username is email
    Optional<User> uOpt = users.findByUsername(emailAddr); // explicit type
    if (uOpt.isEmpty()) {
      return ResponseEntity.badRequest().body(Map.of("error","not found"));
    }

    String token = UUID.randomUUID().toString();
    PasswordResetToken t = new PasswordResetToken(token, uOpt.get().getId(), Instant.now().plusSeconds(900));
    resetRepo.save(t);

    String link = "http://localhost:3000/reset-password/confirm?token=" + token;
    email.sendPasswordResetEmail(emailAddr, link);
    return ResponseEntity.ok(Map.of("message","sent"));
  }

  @PostMapping("/reset-password/confirm")
  public ResponseEntity<?> confirm(@RequestBody Map<String,String> body) {
    String token = body.get("token");
    String npw = body.get("newPassword");

    PasswordResetToken t = resetRepo.findByToken(token).orElse(null); // explicit type
    if (t == null || t.getExpiry().isBefore(Instant.now())) {
      return ResponseEntity.badRequest().body(Map.of("error","invalid"));
    }

    User u = users.findById(t.getUserId()).orElseThrow(); // explicit type
    u.setPasswordHash(encoder.encode(npw));
    users.save(u);
    resetRepo.delete(t);

    return ResponseEntity.ok(Map.of("message","ok"));
  }
}
