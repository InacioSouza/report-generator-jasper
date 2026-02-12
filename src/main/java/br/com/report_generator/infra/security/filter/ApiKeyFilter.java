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
    private static final String CLIENT_ID_HEADER = "X-CLIENT-ID";

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

            if (chaveAPIEnviada == null || chaveAPIEnviada.isEmpty()) throw new FalhaAutenticacaoException(
                    "A chave de API deve ser enviada no cabeçalho da requisição!");

            String clientIdHeader = request.getHeader(CLIENT_ID_HEADER);

            if (clientIdHeader == null) throw new FalhaAutenticacaoException(
                    "O id do sistema que está fazendo a requisição deve ser informado pelo atributo X-CLIENT-ID !");

            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            UUID uuidSistema = null;
            try {
                uuidSistema = UUID.fromString(clientIdHeader);
            } catch (IllegalArgumentException e) {
                throw new FormatoInvalidoException("O id do sistema informado não está no padrão correto (UUID)!");
            }

            List<ApiKey> listApiKey = this.apiKeyService.buscaChavesPorIdSistema(uuidSistema);

            if (listApiKey.isEmpty()) throw new FalhaAutenticacaoException(
                    "Não foi encontrada chave cadastrada para o sistema de id: " + clientIdHeader + " o sistema não possui uma chave ou ele sequer existe ");

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
                        uuidSistema,
                        null,
                        listAuthority
                );

                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                throw new FalhaAutenticacaoException("A chave informada não é válida!");
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean ehSwagger(String uri) {
        return uri.contains("swagger");
    }
}
