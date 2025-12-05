package br.com.report_generator.infra.exception;

public class FalhaAoSalvarTemplateException extends RuntimeException {

    public FalhaAoSalvarTemplateException() {
        super("Ocorreu uma falha ao tentar tratar os bytes do template");
    }
}
