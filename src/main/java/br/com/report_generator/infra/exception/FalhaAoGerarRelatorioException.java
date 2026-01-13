package br.com.report_generator.infra.exception;

public class FalhaAoGerarRelatorioException extends RuntimeException {

    public FalhaAoGerarRelatorioException() {
        super("Ocorreu uma falha ao tentar gerar o relatorio");
    }

    public FalhaAoGerarRelatorioException(String message) {
        super(message);
    }
}
