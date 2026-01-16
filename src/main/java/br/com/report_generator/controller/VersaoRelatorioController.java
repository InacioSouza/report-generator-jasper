package br.com.report_generator.controller;

import br.com.report_generator.dto.versaorelatorio.CadastraVersaoRelatorioRequestDto;
import br.com.report_generator.dto.versaorelatorio.VersaoRelatorioRequestDto;
import br.com.report_generator.infra.exception.RegistroNaoEncontradoException;
import br.com.report_generator.model.Relatorio;
import br.com.report_generator.service.api.RelatorioService;
import br.com.report_generator.service.api.VersaoRelatorioService;
import jakarta.persistence.Access;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/versao-relatorio")
public class VersaoRelatorioController {

    @Autowired
    private RelatorioService relatorioService;

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

        Relatorio relatorio = this.relatorioService.findById(dto.idRelatorio());
        if (relatorio == null) throw new RegistroNaoEncontradoException(
                "Não foi encontrado relatório para o id: " + dto.idRelatorio()
        );

        return ResponseEntity.ok(this.versaoRelatorioService.cadastraVersaoRelatorio(versaoZip, dto, relatorio));
    }

    @GetMapping
    public ResponseEntity<?> buscarTodasVersaoRelatorio() {
        return ResponseEntity.ok(this.versaoRelatorioService.findAll().stream().map(VersaoRelatorioRequestDto::new).toList());
    }

}
