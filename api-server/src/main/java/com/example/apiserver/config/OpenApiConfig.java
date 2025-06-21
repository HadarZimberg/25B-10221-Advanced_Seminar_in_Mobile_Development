package com.example.apiserver.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        Server server = new Server()
                .url("https://faithful-dolphin-map-sdk-platform-27b4e97b.koyeb.app");

        return new OpenAPI()
                .servers(List.of(server))
                .info(new Info()
                        .title("Polygon API")
                        .version("1.0"));
    }
}
