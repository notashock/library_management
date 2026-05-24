package com.library.server.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI()
                .info(new Info().title("Library Book Issue & Return API").version("1.0"))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
    @Bean
    public OperationCustomizer customize() {
        return (operation, handlerMethod) -> {
            boolean isPublic = handlerMethod.getMethod().getDeclaringClass().getName().contains("AuthController")
                    || handlerMethod.getMethod().getName().contains("register"); // simplistic check for the hackathon

            // If it has @PreAuthorize or is not explicitly public, add the security requirement
            if (handlerMethod.getMethodAnnotation(PreAuthorize.class) != null || !isPublic) {
                operation.addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
            }
            return operation;
        };
    }
}