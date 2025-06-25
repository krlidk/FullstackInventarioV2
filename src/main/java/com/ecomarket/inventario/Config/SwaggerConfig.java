package com.ecomarket.inventario.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//URL SWAGGER http://localhost:8081/doc/swagger-ui/index.html#/

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI CustomOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API 2025 Ecomarket Inventario ")
                        .version("1.0")
                        .description("Documentacion API para Ecomarket de inventario de productos."));
    }
}
