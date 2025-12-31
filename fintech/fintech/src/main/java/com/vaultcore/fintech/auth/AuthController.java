package com.vaultcore.fintech.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.vaultcore.fintech.query.PasswordResetToken;
import com.vaultcore.fintech.query.PasswordResetTokenRepo;
import com.vaultcore.fintech.query.RefreshTokenService;
import com.vaultcore.fintech.service.EmailService;
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

    public AuthController(UserRepo users,
                          PasswordEncoder encoder,
                          JwtService jwt,
                          RefreshTokenService refresh,
                          PasswordResetTokenRepo resetRepo,
                          EmailService email) {
        this.users = users;
        this.encoder = encoder;
        this.jwt = jwt;
        this.refresh = refresh;
        this.resetRepo = resetRepo;
        this.email = email;
    }
    public record RegisterRequest(String username, String password, String email) {}
    // -------------------- Registration --------------------
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String pw = body.get("password");
        String emailAddr = body.get("email");

        if (username == null || pw == null || emailAddr == null) {
            throw new IllegalArgumentException("Missing username, password, or email");
        }
        if (users.findByUsername(username).isPresent()) {
            throw new IllegalStateException("User already exists");
        }

        User u = new User();
        u.setUsername(username);
        u.setPasswordHash(encoder.encode(pw));
        u.setRole("USER");
        u.setEmail(emailAddr);   // <-- FIX
        users.save(u);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "User registered successfully"));
    }


    // -------------------- Login --------------------
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String pw = body.get("password");

        if (username == null || pw == null) {
            throw new IllegalArgumentException("Missing username or password");
        }

        User u = users.findByUsername(username).orElseThrow(
                () -> new SecurityException("Invalid credentials")
        );

        if (!encoder.matches(pw, u.getPasswordHash())) {
            throw new SecurityException("Invalid credentials");
        }

        String access = jwt.generateToken(u.getId(), u.getUsername(), u.getRole());
        String rt = refresh.issue(u.getId().toString(), 7 * 24 * 3600); // 7 days

        return ResponseEntity.ok(Map.of("access_token", access, "refresh_token", rt));
    }

    // -------------------- Refresh Token --------------------
    @PostMapping("/token/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> body) {
        String rt = body.get("refresh_token");
        if (rt == null) {
            throw new IllegalArgumentException("Missing refresh_token");
        }

        String userId = refresh.validateAndConsume(rt);
        if (userId == null) {
            throw new SecurityException("Invalid refresh_token");
        }

        User u = users.findById(UUID.fromString(userId)).orElseThrow();
        String access = jwt.generateToken(u.getId(), u.getUsername(), u.getRole());
        String newRt = refresh.issue(userId, 7 * 24 * 3600);

        return ResponseEntity.ok(Map.of("access_token", access, "refresh_token", newRt));
    }

    // -------------------- Password Reset Request --------------------
    @PostMapping("/reset-password")
    public ResponseEntity<?> reset(@RequestBody Map<String, String> body) {
        String emailAddr = body.get("email");
        if (emailAddr == null) {
            throw new IllegalArgumentException("Missing email");
        }

        Optional<User> uOpt = users.findByUsername(emailAddr);
        if (uOpt.isEmpty()) {
            throw new IllegalStateException("User not found");
        }

        String token = UUID.randomUUID().toString();
        User user = uOpt.get();
        PasswordResetToken t = new PasswordResetToken(token, user, Instant.now().plusSeconds(900));
        resetRepo.save(t);

        String link = "http://localhost:3000/reset-password/confirm?token=" + token;
        email.sendPasswordResetEmail(emailAddr, link);

        return ResponseEntity.ok(Map.of("message", "Password reset link sent"));
    }

    // -------------------- Password Reset Confirmation --------------------
    @PostMapping("/reset-password/confirm")
    public ResponseEntity<?> confirm(@RequestBody Map<String, String> body) {
        String token = body.get("token");
        String npw = body.get("newPassword");

        if (token == null || npw == null) {
            throw new IllegalArgumentException("Missing token or newPassword");
        }

        PasswordResetToken t = resetRepo.findByToken(token).orElseThrow(
                () -> new IllegalStateException("Invalid or expired token")
        );

        if (t.getExpiry().isBefore(Instant.now())) {
            throw new IllegalStateException("Invalid or expired token");
        }

        User u = t.getUser();
        u.setPasswordHash(encoder.encode(npw));
        users.save(u);
        resetRepo.delete(t);

        return ResponseEntity.ok(Map.of("message", "Password updated successfully"));
    }
}
