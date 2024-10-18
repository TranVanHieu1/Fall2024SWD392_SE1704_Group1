package com.group1.Care_Koi_System.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CORSConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "http://localhost:5173",
                        "http://localhost:8080",
                        "http://localhost:8000",
                        "https://fall2024swd392-se1704-group1.onrender.com",
                        "https://carekoisystem-chb5b3gdaqfwanfr.canadacentral-01.azurewebsites.net")
                .allowedHeaders("*")
                .exposedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Methods","Access-Control-Allow-Headers")
                .allowedMethods("*")
                .maxAge(1440000);
    }

}