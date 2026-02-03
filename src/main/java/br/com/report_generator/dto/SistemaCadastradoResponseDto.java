package br.com.report_generator.dto;

import java.util.UUID;

public record SistemaCadastradoResponseDto(
        UUID sistemaId,
        String chaveAPI
) {
}
