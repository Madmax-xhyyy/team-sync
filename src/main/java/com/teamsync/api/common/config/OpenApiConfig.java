package com.teamsync.api.common.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI teamSyncOpenAPI() {

    final String securityScheme = "Bearer Authentication";

    return new OpenAPI()

        .info(
            new Info()
                .title("TeamSync API")
                .version("v1")
                .description("""
                    TeamSync REST API

                    Team collaboration platform supporting:

                    • Organizations
                    • Projects
                    • Task Boards
                    • Tasks
                    • Comments
                    • Activity Logs
                    """)
                .contact(
                    new Contact()
                        .name("TeamSync")
                        .email("support@teamsync.com"))
                .license(
                    new License()
                        .name("MIT")))

        .addSecurityItem(
            new SecurityRequirement()
                .addList(securityScheme))

        .schemaRequirement(
            securityScheme,
            new SecurityScheme()
                .name(securityScheme)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT"))

        .externalDocs(
            new ExternalDocumentation()
                .description("TeamSync Documentation"));

  }

}
