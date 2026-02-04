package br.com.report_generator.infra.config;

/**
 * Classe para armazenar o prefixo dos endpoints na API,
 * útil para diferenciar os tipos de endpoints.
 * Muito cuidado ao alterar o valor de qualquer constante nesta classe,
 * pois isso terá impacto em todos os endpoints.
* */
public final class EndpointPrefix {

    private EndpointPrefix() {}

    public static final String API = "/api-report";
    public static final String ADMIN = "/admin";
}
