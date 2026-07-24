package com.teamsync.api.common.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI teamSyncOpenAPI() {
    return new OpenAPI()
      .info(new Info()
        .title("TeamSync API")
        .description("REST API for TeamSync Team Collaboration Platform")
        .version("v1.0.0")
        .contact(new Contact()
          .name("TeamSync")
          .email("admin@teamsync.com")))
      .externalDocs(new ExternalDocumentation()
          .description("TeamSync Documentation"));
  }

}
