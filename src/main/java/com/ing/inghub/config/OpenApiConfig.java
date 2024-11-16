package com.ing.inghub.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "REST API", version = "1.0", description = "REST API description...", contact = @Contact(name = "Onat Kutlu")),
    security = {@SecurityRequirement(name = "bearerToken")})
@SecurityScheme(name = "bearerToken", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")
public class OpenApiConfig {

  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(new io.swagger.v3.oas.models.info.Info()
            .title("Assignment API")
            .version("1.0")
            .description("Assignment API")
            .termsOfService("http://swagger.io/terms/")
            .license(new License()
                .name("Apache 2.0")
                .url("http://springdoc.org")
            )
            .contact(new io.swagger.v3.oas.models.info.Contact()
                .email("onatkutlu97@gmail.com")
                .name("Onat Kutlu")
                .url("https://linkedin.com/in/onat-kutlu-044580176")
            )
        );
  }
}