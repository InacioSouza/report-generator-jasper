package br.com.report_generator.dto;

public record PdfGeradoDto(
        String nome,
        byte[] pdf
) {
}
