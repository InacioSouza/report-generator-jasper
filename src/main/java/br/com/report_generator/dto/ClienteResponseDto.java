package br.com.report_generator.dto;

import br.com.report_generator.model.Cliente;

import java.util.UUID;

public record ClienteResponseDto(
        UUID id,
        String nome
) {
    public ClienteResponseDto(Cliente cliente) {
        this(cliente.getId(), cliente.getNome());
    }
}
