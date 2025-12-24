package com.vaultcore.fintech.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

import com.vaultcore.fintech.risk.FraudInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {
private final FraudInterceptor fraudInterceptor;
public WebConfig(FraudInterceptor fraudInterceptor) { this.fraudInterceptor = fraudInterceptor; }

@Override
public void addInterceptors(InterceptorRegistry registry) {
 registry.addInterceptor(fraudInterceptor).addPathPatterns("/api/transfers/**");
}
}
