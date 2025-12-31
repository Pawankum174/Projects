package com.vaultcore.fintech.risk;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.vaultcore.fintech.users.User;
import com.vaultcore.fintech.users.UserRepo;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * Intercepts transfer requests; flags high-risk amounts with a 2FA challenge.
 * For simplicity, reads amount from header. In production, use AOP on validated DTO.
 */
@Component
public class FraudInterceptor implements HandlerInterceptor {

    @Value("${vaultcore.fraud.amount.threshold:10000}")
    private BigDecimal threshold;

    private final ChallengeService challengeService;
    private final UserRepo userRepo;

    public FraudInterceptor(ChallengeService challengeService, UserRepo userRepo) {
        this.challengeService = challengeService;
        this.userRepo = userRepo;
    }

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws IOException {
        if (req.getRequestURI().startsWith("/api/transfers") && "POST".equals(req.getMethod())) {
            String amt = req.getHeader("X-Transfer-Amount");
            if (amt != null) {
                BigDecimal amount = new BigDecimal(amt);
                if (amount.compareTo(threshold) >= 0) {
                    String email = req.getHeader("X-User-Email");
                    UUID userId = userRepo.findByEmail(email)
                            .map(User::getId)
                            .orElseThrow(() -> new IllegalArgumentException("Unknown user"));

                    challengeService.issueChallenge(userId);

                    res.setStatus(202);
                    res.setContentType("application/json");
                    res.getWriter().write("{\"challenge_required\":true}");
                    return false;
                }
            }
        }
        return true;
    }
}
