package com.chrisgya.springsecurity.config;

import com.chrisgya.springsecurity.config.properties.FrontEndUrlProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private FrontEndUrlProperties frontEndUrlProperties;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedOrigins(frontEndUrlProperties.getBaseUrl())
                .allowedHeaders("*");
    }
}
