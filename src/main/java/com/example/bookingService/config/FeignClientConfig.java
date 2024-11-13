package com.example.bookingService.config;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                // Add basic authentication header
                String auth = "admin:admin"; // Replace with actual credentials
                String encodedAuth = "Basic " + java.util.Base64.getEncoder().encodeToString(auth.getBytes());
                template.header("Authorization", encodedAuth);
            }
        };
    }
}
