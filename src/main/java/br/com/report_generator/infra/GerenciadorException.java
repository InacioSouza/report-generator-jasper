package br.com.report_generator.infra;

import br.com.report_generator.infra.exception.FalhaAoSalvarRelatorioException;
import br.com.report_generator.infra.exception.FormatoArquivoInvalidoException;
import br.com.report_generator.infra.exception.RegistroCorrompidoException;
import br.com.report_generator.infra.exception.RegistroNaoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GerenciadorException {

    @ExceptionHandler(FalhaAoSalvarRelatorioException.class)
    public ResponseEntity<?> falhaAoSalvarRelatorio(FalhaAoSalvarRelatorioException exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
    }

    @ExceptionHandler(FormatoArquivoInvalidoException.class)
    public ResponseEntity<?> formatoArquivoinvalido(FormatoArquivoInvalidoException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(RegistroNaoEncontradoException.class)
    public ResponseEntity<?> registroNaoEncontrado(RegistroNaoEncontradoException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(RegistroCorrompidoException.class)
    public ResponseEntity<?> registroCorrompido(RegistroCorrompidoException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }
}
