package com.vaultcore.fintech.config;

import com.vaultcore.fintech.risk.FraudInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final FraudInterceptor fraudInterceptor;

    public WebConfig(FraudInterceptor fraudInterceptor) {
        this.fraudInterceptor = fraudInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Register FraudInterceptor for all paths, or narrow it down
        registry.addInterceptor(fraudInterceptor)
                .addPathPatterns("/api/transfers/**");
    }
}
