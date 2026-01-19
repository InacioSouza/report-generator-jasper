package br.com.report_generator.controller;

import br.com.report_generator.dto.SistemaResponseDto;
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
    public ResponseEntity<List<SistemaResponseDto>> buscaTodos() {
        return ResponseEntity.ok(this.service.findAll().stream().map(SistemaResponseDto::new).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SistemaResponseDto> buscaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(new SistemaResponseDto(this.service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<SistemaResponseDto> cadastra(@RequestBody SistemaRequestDto sistemaRequest) {
        Sistema sistema = new Sistema();
        sistema.setNome(sistemaRequest.nome());
        sistema.setDescricao(sistemaRequest.descricao());
        return ResponseEntity.ok(new SistemaResponseDto(this.service.save(sistema)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SistemaResponseDto> atualiza(
            @PathVariable Long id,
            @RequestBody SistemaRequestDto dto
    ) {
        return ResponseEntity.ok(this.service.atualiza(id, dto));
    }
}
