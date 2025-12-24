package com.vaultcore.fintech.risk;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Intercepts transfer requests; flags high-risk amounts with a 2FA challenge.
 * For simplicity, reads amount from header. In production, use AOP on validated DTO.
 */
@Component
public class FraudInterceptor implements HandlerInterceptor {

  @Value("${vaultcore.fraud.amount.threshold:10000}")
  private BigDecimal threshold;

  private final ChallengeService challengeService;

  public FraudInterceptor(ChallengeService challengeService) {
    this.challengeService = challengeService;
  }

  @Override
  public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws IOException {
    if (req.getRequestURI().startsWith("/api/transfers") && "POST".equals(req.getMethod())) {
      String amt = req.getHeader("X-Transfer-Amount");
      if (amt != null) {
        BigDecimal amount = new BigDecimal(amt);
        if (amount.compareTo(threshold) >= 0) {
          String userId = req.getHeader("X-User-Email"); // demo: email as identifier
          challengeService.issueChallenge(userId == null ? "unknown@local" : userId);
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
