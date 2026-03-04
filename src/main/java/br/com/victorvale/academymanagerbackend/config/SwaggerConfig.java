package br.com.victorvale.academymanagerbackend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI(){
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI().addSecurityItem(new SecurityRequirement()
                .addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT"))).info(new Info().title("ACADEMY MANAGER")
                        .contact(contact())
                        .description("SISTEMA DE GERENCIAMENTO DE ACADEMIA DE LUTAS")
                        .license(license())
                        .version("1.0.0"));
    }

    private Contact contact() {
        return new Contact().name("Victor Vale").email("joaovictorvale.dev@gmail.com");
    }

    private License license() {
        return new License()
                .url("\"https://www.apache.org/licenses/LICENSE-2.0\\\"")
                .name("Apache License Version 2.0\"");
    }
}
