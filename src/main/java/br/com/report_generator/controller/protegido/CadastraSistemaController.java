package br.com.report_generator.controller.protegido;

import br.com.report_generator.dto.SistemaRequestDto;
import br.com.report_generator.dto.SistemaResponseDto;
import br.com.report_generator.model.Sistema;
import br.com.report_generator.service.api.SistemaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/cadastra-sistema")
public class CadastraSistemaController {

    private final SistemaService sistemaService;

    public CadastraSistemaController(SistemaService sistemaService) {
        this.sistemaService = sistemaService;
    }

    @PostMapping
    public ResponseEntity<String> cadastra(@RequestBody SistemaRequestDto sistemaRequest) {
        Sistema sistema = new Sistema();
        sistema.setNome(sistemaRequest.nome());
        sistema.setDescricao(sistemaRequest.descricao());
        new SistemaResponseDto(this.sistemaService.save(sistema));
        return ResponseEntity.ok(null);
    }
}
