package io.jprime.agenticrag.videoproductionstore.web.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Video Production Store API")
                        .description("REST API for managing professional video broadcast equipment inventory, customers and orders")
                        .version("6.0.0"));
    }
}
