package br.com.report_generator.infra.exception;

public class RegistroCorrompidoException extends RuntimeException{
    public RegistroCorrompidoException(String message) {
        super(message);
    }
}
