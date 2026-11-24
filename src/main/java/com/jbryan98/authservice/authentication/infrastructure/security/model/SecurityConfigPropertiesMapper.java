package com.jbryan98.authservice.authentication.infrastructure.security.model;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "application.security")
public record SecurityConfigPropertiesMapper(
        JWT jwt,
        Cors cors
) {

    public record JWT(
            String secretKey,
            long expiration
    ) {
    }

    public record Cors(
            List<String> allowedOrigins,
            List<String> allowedMethods,
            List<String> allowedHeaders
    ) {
    }

}
