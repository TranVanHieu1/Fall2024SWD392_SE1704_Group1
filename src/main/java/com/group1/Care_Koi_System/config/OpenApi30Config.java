package com.group1.Care_Koi_System.config;

//import io.swagger.v3.oas.annotations.OpenAPIDefinition;
//import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
//
//import io.swagger.v3.oas.models.security.SecurityScheme;
//import io.swagger.v3.oas.models.Components;
//import io.swagger.v3.oas.models.OpenAPI;
//import io.swagger.v3.oas.models.security.SecurityRequirement;
//import io.swagger.v3.oas.models.servers.Server;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import io.swagger.v3.oas.models.info.Info;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;
import java.util.List;


@Configuration

@OpenAPIDefinition(
        info = @Info(
                title = "Care-Koi",
                version = "1.0.0"
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Development Server URL"),
                @Server(url = "https://carekoisystem-chb5b3gdaqfwanfr.canadacentral-01.azurewebsites.net", description = "Azure Server URL")
        },
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OpenApi30Config {
//    @Bean
//    public OpenAPI myOpenAPI() {
//        Server devServer1 = new Server();
//        devServer1.setUrl("https://fall2024swd392-se1704-group1.onrender.com");
//        devServer1.setDescription("Production Server URL");
//
//        Server devServer2 = new Server();
//        devServer2.setUrl("http://localhost:8080");
//        devServer2.setDescription("Development Server URL");
//
//        Info info = new Info()
//                .title("Care-Koi")
//                .version("1.0.0");
//
//        return new OpenAPI().info(info)
//                .servers(List.of(devServer1, devServer2)) // Ensure Java 9+ or use Collections.singletonList(devServer)
//                .addSecurityItem(new SecurityRequirement()
//                        .addList("bearerAuth"))  // Make sure this matches 'bearerAuth'
//                .components(new Components()
//                        .addSecuritySchemes("bearerAuth", createAPIKeyScheme())); // Make sure this matches 'bearerAuth'
//    }
//
//    private SecurityScheme createAPIKeyScheme() {
//        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
//                .bearerFormat("JWT")
//                .scheme("bearer");
//    }
}
