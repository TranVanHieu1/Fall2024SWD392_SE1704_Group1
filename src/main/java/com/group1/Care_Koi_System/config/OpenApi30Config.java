package com.group1.Care_Koi_System.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;

import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.info.Info;

import java.util.List;


@Configuration

public class OpenApi30Config {
    @Bean
    public OpenAPI myOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl("https://fall2024swd392-se1704-group1.onrender.com");
        devServer.setUrl("http://localhost:8080");
        devServer.setDescription("Server URL in Development environment");

        Info info = new Info()
                .title("Care-Koi")
                .version("1.0.0");

        return new OpenAPI().info(info)
                .servers(List.of(devServer)) // Ensure Java 9+ or use Collections.singletonList(devServer)
                .addSecurityItem(new SecurityRequirement()
                        .addList("bearerAuth"))  // Make sure this matches 'bearerAuth'
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", createAPIKeyScheme())); // Make sure this matches 'bearerAuth'
    }

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }
}
