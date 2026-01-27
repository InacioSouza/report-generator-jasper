package br.com.report_generator.infra.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class AdminSecurityFilter extends OncePerRequestFilter {

    @Value("${sec.ip.admin}")
    private String ipAdmin;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        if(request.getRequestURI().contains("/admin")
                && request.getRemoteAddr().equals(ipAdmin)
                && SecurityContextHolder.getContext().getAuthentication() == null
        ) {

            List<GrantedAuthority> listAuthority = List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));

            Authentication auth = new UsernamePasswordAuthenticationToken(
                    "admin-ip",
                    null,
                    listAuthority
            );

            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }
}
