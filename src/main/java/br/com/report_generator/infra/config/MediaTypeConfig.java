package br.com.report_generator.infra.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class MediaTypeConfig {

    // Configuração necessária para enviar arquivos pelo Sweagger via multipart/form-data
    public MediaTypeConfig(MappingJackson2HttpMessageConverter converter) {

        List<MediaType> supportedMediaTypes = new ArrayList<>(converter.getSupportedMediaTypes());
        supportedMediaTypes.add(new MediaType("application", "octet-stream"));
        converter.setSupportedMediaTypes(supportedMediaTypes);

    }
}
