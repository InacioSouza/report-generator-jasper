package br.com.report_generator.infra.exception.handler;

import br.com.report_generator.infra.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;

@RestControllerAdvice
public class GerenciadorException {

    @ExceptionHandler(AutorizationException.class)
    public ResponseEntity<ExceptionMessage> erroOperacaoNaoAutorizada(AutorizationException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body (
                new ExceptionMessage(new Date(), 401, exception.getMessage())
        );
    }

    @ExceptionHandler(FalhaAoSalvarRelatorioException.class)
    public ResponseEntity<ExceptionMessage> falhaAoSalvarRelatorio(FalhaAoSalvarRelatorioException exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ExceptionMessage(new Date(), 401, exception.getMessage())
        );
    }

    @ExceptionHandler(FalhaAoGerarRelatorioException.class)
    public ResponseEntity<ExceptionMessage> falhaAoGerarRelatorio(FalhaAoGerarRelatorioException exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body (
                new ExceptionMessage(new Date(), 401, exception.getMessage())
        );
    }

    @ExceptionHandler(FormatoInvalidoException.class)
    public ResponseEntity<ExceptionMessage> formatoinvalido(FormatoInvalidoException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ExceptionMessage(new Date(), 401, exception.getMessage())
        );
    }

    @ExceptionHandler(RegistroNaoEncontradoException.class)
    public ResponseEntity<ExceptionMessage> registroNaoEncontrado(RegistroNaoEncontradoException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ExceptionMessage(new Date(), 401, exception.getMessage())
        );
    }

    @ExceptionHandler(RegistroCorrompidoException.class)
    public ResponseEntity<ExceptionMessage> registroCorrompido(RegistroCorrompidoException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ExceptionMessage(new Date(), 401, exception.getMessage())
        );
    }

    @ExceptionHandler(FalhaAutenticacaoException.class)
    public ResponseEntity<ExceptionMessage> falhaAutenticacao(FalhaAutenticacaoException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new ExceptionMessage(new Date(), 401, exception.getMessage())
        );
    }

    @ExceptionHandler(FalhaCompilacaoJasperException.class)
    public ResponseEntity<ExceptionMessage> falhaCompilacaoJasperException(
            FalhaCompilacaoJasperException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ExceptionMessage(new Date(), 400, exception.getMessage())
        );
    }
}
