package br.com.report_generator.infra.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        String descricao = """
                Documentação dos end-points disponíveis na API.
                Os end-points que contém 'api-r' na url devem ser autenticados via chave de API,
                já os end-points que contén '/admin' devem ser autenticados por Basic Auth (username e senha)
                """;

        return new OpenAPI()
                .info(new Info()
                        .title("Report Generator API")
                        .version("1.0")
                        .description(descricao)
                        .contact(new Contact()
                                .name("Inácio Souza Rocha")
                                .url("https://www.linkedin.com/in/inacio-souza/"))
                )
                .components(new Components()
                        .addSecuritySchemes("basicAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("basic"))
                        .addSecuritySchemes("apiKeyAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name("X-API-KEY"))
                        .addSecuritySchemes("clientIdAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name("X-CLIENT-ID"))
                );
    }
}
