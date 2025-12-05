package br.com.report_generator.infra.exception;

public class FormatoArquivoInvalidoException extends RuntimeException {
    public FormatoArquivoInvalidoException() {
        super("Formato de arquivo inválido! (deferente de '*.jrxm' ou '*.jasper')");
    }

    public FormatoArquivoInvalidoException(String message) {
        super(message);
    }
}
