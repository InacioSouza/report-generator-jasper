package br.com.report_generator.infra.exception.handler;

import java.util.Date;

public record ExceptionMessage (
        Date timestamp,
        Integer status,
        String message
) {
}
