package br.com.report_generator.dto;

import java.util.UUID;

public record ClienteCadastradoResponseDto(
        UUID clienteId,
        String chaveAPI
) {
}
