package br.com.report_generator.infra.exception;

public class ZipVazioException extends RuntimeException {

    public ZipVazioException() {
        super("Não existem arquivos no zip enviado!");
    }

    public ZipVazioException(String message) {
        super(message);
    }
}
