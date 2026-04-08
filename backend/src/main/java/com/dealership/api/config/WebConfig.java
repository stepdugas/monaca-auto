package com.dealership.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Additional MVC configuration.
 * CORS is handled in SecurityConfig; this handles any static resource needs.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Add static resource mappings here if needed (e.g., serving uploaded files locally)
    }
}
