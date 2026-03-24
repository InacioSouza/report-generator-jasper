package br.com.report_generator.infra.exception;

public class NomenclaturaArquivoInvalidaException extends RuntimeException {

    public NomenclaturaArquivoInvalidaException() {
        super("O nome do arquivo enviado é inválido!");
    }

    public NomenclaturaArquivoInvalidaException(String message) {
        super(message);
    }
}
