package br.com.report_generator.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record PedidoRelatorioDTO (String titulo,
                                  String subtitulo,
                                  @NotNull @NotBlank
                                  UUID idRelatorio,
                                  @NotNull @NotBlank
                                  UUID idVersao,
                                  @NotNull
                                  List<Map<String, Object>> dataSource,
                                  @NotNull
                                  ExibicaoRelatorio exibicaoRelatorio) {
}
