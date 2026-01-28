package br.com.report_generator.service.utils;

import java.util.UUID;

public class ApiKeyGenerator {
    public static String gerar() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
