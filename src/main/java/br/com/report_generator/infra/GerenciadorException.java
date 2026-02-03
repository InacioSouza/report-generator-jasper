package br.com.report_generator.infra;

import br.com.report_generator.infra.exception.*;
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

    @ExceptionHandler(FalhaAoGerarRelatorioException.class)
    public ResponseEntity<?> falhaAoGerarRelatorio(FalhaAoGerarRelatorioException exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
    }

    @ExceptionHandler(FormatoInvalidoException.class)
    public ResponseEntity<?> formatoinvalido(FormatoInvalidoException exception) {
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

    @ExceptionHandler(FalhaAutenticacaoException.class)
    public ResponseEntity<?> falhaAutenticacao(FalhaAutenticacaoException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getMessage());
    }
}
