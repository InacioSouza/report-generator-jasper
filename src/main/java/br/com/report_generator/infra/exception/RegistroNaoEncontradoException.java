package br.com.report_generator.infra.exception;

public class RegistroNaoEncontradoException extends RuntimeException{
    public RegistroNaoEncontradoException(String message) {
        super(message);
    }
}
