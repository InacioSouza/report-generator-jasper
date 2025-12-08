package br.com.report_generator.dto;

import br.com.report_generator.dto.sistema.SistemaDto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record CadastraRelatorioDto(
        @NotNull
        String titulo,
        String subtitulo,
        String nome,
        String informacao,
        String descricaoTecnica,
        String descricaoVersao,
        @NotNull
        SistemaDto sistema
) {
}
