package com.jbryan98.authservice.shared;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

import java.util.List;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Auth Service API",
                version = "1.0",
                description = "This API is a demo for a Spring Security application using JWT for authentication and authorization.",
                contact = @Contact(
                        name = "Bryan Corrales",
                        url = "https://github.com/JBryan98",
                        email = "bryan.corrales230198@gmail.com"
                )
        ),
        security = @SecurityRequirement(name = "Security token")
)
@SecurityScheme(
        name = "Security token",
        description = "Access token for secured endpoints",
        type = SecuritySchemeType.HTTP,
        paramName = HttpHeaders.AUTHORIZATION,
        in = SecuritySchemeIn.HEADER,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class SwaggerConfig {

    /**
     * This bean removes the security requirement from the public endpoints (e.g., /api/v1/auth/**) in the OpenAPI documentation.
     */
    @Bean
    public OpenApiCustomizer removeSecurityFromPublicEndpoints() {
        return openApi -> {
            if (openApi.getPaths() == null) return;
            openApi.getPaths().forEach((path, pathItem) -> {
                if(path.startsWith("/auth")) {
                    pathItem.readOperations().forEach(operation -> operation.setSecurity(List.of()));
                }
            });
        };
    }
}
