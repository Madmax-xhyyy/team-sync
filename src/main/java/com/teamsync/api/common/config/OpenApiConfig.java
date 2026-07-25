package com.teamsync.api.common.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "bearer")
public class OpenApiConfig {

  @Bean
  public OpenAPI teamSyncOpenAPI() {

    return new OpenAPI()

        .info(new Info()

            .title("TeamSync API")

            .version("v1")

            .description("REST API for TeamSync Project Management System.")

            .contact(new Contact()

                .name("TeamSync")

                .email("support@teamsync.com"))

            .license(new License()

                .name("MIT")))

        .externalDocs(new ExternalDocumentation()

            .description("TeamSync Documentation"));

  }

}
