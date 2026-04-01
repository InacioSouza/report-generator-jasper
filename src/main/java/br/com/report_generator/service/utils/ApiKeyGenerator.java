package br.com.report_generator.service.utils;

import java.util.UUID;

public class ApiKeyGenerator {
    public static String gerar(UUID idCliente) {
        return UUID.randomUUID().toString().replace("-", "") + "." + idCliente;
    }
}
