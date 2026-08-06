package co.homecenter.etq.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI etqOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ETQ Print API")
                        .description("Submodulo de impresion de ETQ - prueba tecnica")
                        .version("1.0.0"));
    }
}
