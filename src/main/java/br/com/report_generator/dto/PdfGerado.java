package br.com.report_generator.dto;

public record PdfGerado(
        String nome,
        byte[] pdf
) {
}
