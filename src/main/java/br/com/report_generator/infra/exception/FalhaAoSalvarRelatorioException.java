package br.com.report_generator.infra.exception;

public class FalhaAoSalvarRelatorioException extends RuntimeException {

    public FalhaAoSalvarRelatorioException() {
        super("Ocorreu uma falha ao tentar tratar os bytes do relatorio");
    }

    public FalhaAoSalvarRelatorioException(String message) {
        super(message);
    }
}
