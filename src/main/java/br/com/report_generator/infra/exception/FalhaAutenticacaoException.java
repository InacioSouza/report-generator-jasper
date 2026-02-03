package br.com.report_generator.infra.exception;

public class FalhaAutenticacaoException extends RuntimeException {

    public FalhaAutenticacaoException() {
        super("Ocorreu uma falha ao tentar se autenticar no sistema");
    }

    public FalhaAutenticacaoException(String message) {
        super(message);
    }
}
