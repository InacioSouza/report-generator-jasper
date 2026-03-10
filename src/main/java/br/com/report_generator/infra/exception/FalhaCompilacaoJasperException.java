package br.com.report_generator.infra.exception;

public class FalhaCompilacaoJasperException extends RuntimeException {

    public FalhaCompilacaoJasperException(String message) {
        super(message);
    }

    public FalhaCompilacaoJasperException() {
        super("Falha ao tentar compilar template jrxml. Pode haver algum erro de sintaxe no arquivo.");
    }
}
