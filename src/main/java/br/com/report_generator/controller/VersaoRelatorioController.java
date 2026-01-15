package br.com.report_generator.controller;

import br.com.report_generator.dto.CadastraVersaoRelatorioDto;
import br.com.report_generator.service.api.VersaoRelatorioService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
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
                                                          CadastraVersaoRelatorioDto dto) {
        return ResponseEntity.ok(this.versaoRelatorioService.cadastraVersaoRelatorio(versaoZip, dto));
    }

}
