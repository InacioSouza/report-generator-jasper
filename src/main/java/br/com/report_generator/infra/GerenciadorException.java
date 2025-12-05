package br.com.report_generator.infra;

import br.com.report_generator.infra.exception.FalhaAoSalvarTemplateException;
import br.com.report_generator.infra.exception.FormatoArquivoInvalidoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GerenciadorException {

    @ExceptionHandler(FalhaAoSalvarTemplateException.class)
    public ResponseEntity<?> falhaAoSalvarTemplate(FalhaAoSalvarTemplateException exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
    }

    @ExceptionHandler(FormatoArquivoInvalidoException.class)
    public ResponseEntity<?> formatoArquivoinvalido(FormatoArquivoInvalidoException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }
}
