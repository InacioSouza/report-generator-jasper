package br.com.report_generator.infra.exception;

import br.com.report_generator.dto.IdentificadorArquivoPrincipalEnum;

public class IdentificadorArquivoPrincipalInvalidoException extends RuntimeException{

    public IdentificadorArquivoPrincipalInvalidoException() {
        super("Para relatórios com múltiplos arquivos o trecho" + IdentificadorArquivoPrincipalEnum.MAIN + " deve estar no nome do arquivo principal!");
    }

    public IdentificadorArquivoPrincipalInvalidoException(String message) {
        super(message);
    }
}
