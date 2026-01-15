package br.com.report_generator.controller;

import br.com.report_generator.dto.versaorelatorio.CadastraVersaoRelatorioRequestDto;
import br.com.report_generator.dto.versaorelatorio.VersaoRelatorioRequestDto;
import br.com.report_generator.service.api.VersaoRelatorioService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/versao-relatorio")
public class VersaoRelatorioController {

    private final VersaoRelatorioService versaoRelatorioService;

    public VersaoRelatorioController(VersaoRelatorioService versaoRelatorioService) {
        this.versaoRelatorioService = versaoRelatorioService;
    }

    @PostMapping(
            path = "/nova-versao",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> cadastrarNovaVersaoRelatorio(@RequestPart("versaoZip") MultipartFile versaoZip,
                                                          @RequestPart("inf,os") @Valid
                                                          CadastraVersaoRelatorioRequestDto dto) {
        return ResponseEntity.ok(this.versaoRelatorioService.cadastraVersaoRelatorio(versaoZip, dto));
    }

    @GetMapping
    public ResponseEntity<?> listarVersaoRelatorio() {
        return ResponseEntity.ok(this.versaoRelatorioService.findAll().stream().map(VersaoRelatorioRequestDto::new).toList());
    }

}
