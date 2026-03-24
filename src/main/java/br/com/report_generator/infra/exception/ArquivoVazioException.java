package br.com.report_generator.infra.exception;

public class ArquivoVazioException extends RuntimeException {

    public ArquivoVazioException() {
        super("O arquivo enviado está vazio!");
    }

    public ArquivoVazioException(String message) {
        super(message);
    }
}
