package br.com.report_generator.infra.security.filter;

import br.com.report_generator.infra.config.EndpointPrefix;
import br.com.report_generator.infra.exception.FalhaAutenticacaoException;
import br.com.report_generator.infra.exception.FormatoInvalidoException;
import br.com.report_generator.model.ApiKey;
import br.com.report_generator.service.api.ApiKeyService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {

    private static final String API_KEY_HEADER = "X-API-KEY";

    private final ApiKeyService apiKeyService;

    public ApiKeyFilter(ApiKeyService apiKeyService) {
        this.apiKeyService = apiKeyService;
    }

    @Override
    @Order(2)
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        if(request.getRequestURI().contains(EndpointPrefix.API)
                && !this.ehSwagger(request.getRequestURI())
               && SecurityContextHolder.getContext().getAuthentication() == null
        ) {

            String chaveAPIEnviada = request.getHeader(API_KEY_HEADER);

            if (chaveAPIEnviada == null || chaveAPIEnviada.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write(
                        "{\"erro\": \"A chave de API deve ser enviada no cabeçalho da requisição!\"}");
                return;
            }

            String idClienteParam = "";

            String[] partesChave = chaveAPIEnviada.split("\\.");
            if (partesChave.length == 2) {
                idClienteParam = partesChave[1];
            }

            if (idClienteParam.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write(
                        "{\"erro\": \"Chave incorreta! Erro na autenticacao do cliente!\"}");
                return;
            }

            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            UUID uuidCliente = null;
            try {
                uuidCliente = UUID.fromString(idClienteParam);
            } catch (IllegalArgumentException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write(
                        "{\"erro\": \"A chave está fora de padrão\"}");
                return;
            }

            List<ApiKey> listApiKey = this.apiKeyService
                    .buscaChavesPorIdCliente(uuidCliente);

            if (listApiKey.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write(
                        "{\"erro\": \"O cliente que está tentando acessar o sistema não possui uma chave ou não está cadastrado\"}");
                return;
            }

            boolean chaveValida = false;

            for(ApiKey apiKey : listApiKey) {
                if (passwordEncoder.matches(chaveAPIEnviada, apiKey.getHash())
                    && apiKey.isAtiva()) {
                    chaveValida = true;
                    break;
                }
            }

            if (chaveValida) {
                List<GrantedAuthority> listAuthority = List.of(new SimpleGrantedAuthority("ROLE_USER"));

                Authentication auth = new UsernamePasswordAuthenticationToken(
                        uuidCliente,
                        null,
                        listAuthority
                );

                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write(
                        "{\"erro\": \"A chave informada não é válida!\"}");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean ehSwagger(String uri) {
        return uri.contains("swagger");
    }
}
