package br.com.report_generator.controller;

import br.com.report_generator.dto.SistemaDto;
import br.com.report_generator.dto.SistemaRequestDto;
import br.com.report_generator.model.Sistema;
import br.com.report_generator.service.api.SistemaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sistema")
public class SistemaController {

    private final SistemaService service;

    public SistemaController(SistemaService sistemaService) {
        this.service = sistemaService;
    }

    @GetMapping
    public ResponseEntity<List<SistemaDto>> buscaTodos() {
        return ResponseEntity.ok(this.service.findAll().stream().map(SistemaDto::new).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SistemaDto> buscaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(new SistemaDto(this.service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<SistemaDto> cadastra(@RequestBody SistemaRequestDto sistemaRequest) {
        Sistema sistema = new Sistema();
        sistema.setNome(sistemaRequest.nome());
        sistema.setDescricao(sistemaRequest.descricao());
        sistema.setVersao(1);
        return ResponseEntity.ok(new SistemaDto(this.service.save(sistema)));
    }
}
