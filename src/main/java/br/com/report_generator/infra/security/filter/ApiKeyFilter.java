package br.com.report_generator.infra.security.filter;

import br.com.report_generator.model.ApiKey;
import br.com.report_generator.repository.ApiKeyRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
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

@Component
public class ApiKeyFilter extends OncePerRequestFilter {

    private static final String API_KEY_HEADER = "X-API-KEY";

    @Autowired
    private ApiKeyRepository apiKeyRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        if(request.getRequestURI().contains("/api")
        && SecurityContextHolder.getContext().getAuthentication() == null) {

            String chaveAPIEnviada = request.getHeader(API_KEY_HEADER);

            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            ApiKey apiKey = this.apiKeyRepository.findByHash(passwordEncoder.encode(chaveAPIEnviada));

            boolean chaveValida = apiKey != null
                    && apiKey.isAtiva()
                    && apiKey.getHash().equals(chaveAPIEnviada);

            if (chaveValida) {
                List<GrantedAuthority> listAuthority = List.of(new SimpleGrantedAuthority("ROLE_USER"));

                Authentication auth = new UsernamePasswordAuthenticationToken(
                        "user",
                        null,
                        listAuthority
                );

                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        filterChain.doFilter(request, response);
    }
}
