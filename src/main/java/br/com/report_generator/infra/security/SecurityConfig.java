package br.com.report_generator.infra.security;

import br.com.report_generator.infra.security.filter.ApiKeyFilter;
import br.com.report_generator.service.api.ApiKeyService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final ApiKeyService apiKeyService;

    public SecurityConfig(ApiKeyService apiKeyService) {
        this.apiKeyService = apiKeyService;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain adminFilterChain(HttpSecurity http) throws Exception {

        http.securityMatcher("/admin/**")
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers("/admin/**")
                                .hasRole("ADMIN")
                )
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic( httpBasic -> httpBasic
                        .authenticationEntryPoint(desabilitaPopupEmBasicAuth())
                );

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/api-r/**")
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(new ApiKeyFilter(apiKeyService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationEntryPoint desabilitaPopupEmBasicAuth() {
        return (request, response, auth) -> {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        };
    }
}
