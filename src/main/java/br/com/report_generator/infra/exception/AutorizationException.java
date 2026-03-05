package br.com.report_generator.infra.exception;

public class AutorizationException extends RuntimeException {

    public AutorizationException(String message) {
        super(message);
    }

    public AutorizationException() {
        super("Você não pode alterar um registro sem ser o dono dele!");
    }
}
