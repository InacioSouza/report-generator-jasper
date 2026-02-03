package br.com.report_generator.infra.exception;

public class FormatoInvalidoException extends RuntimeException {

    public FormatoInvalidoException() {
        super("Formato de arquivo inválido! (diferente de '*.jrxm' ou '*.jasper')");
    }

    public FormatoInvalidoException(String message) {
        super(message);
    }
}
