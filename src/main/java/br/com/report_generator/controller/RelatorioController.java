package br.com.report_generator.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/relatorio")
public class RelatorioController {

    @PostMapping
    public ResponseEntity<?> cadastraRelatorio() {
        return null;
    }

}
