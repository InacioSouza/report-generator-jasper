package br.com.report_generator.dto;

import br.com.report_generator.dto.sistema.SistemaDto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record CadastraTemplateDto(
        @NotNull
        String titulo,
        String subtitulo,
        String nomeTemplate,
        String informacaoRelatorio,
        String descricaoTemplate,
        String descricaoVersao,
        @NotNull
        @NotEmpty
        MultipartFile arquivoOriginal,
        @NotNull
        SistemaDto sistema
) {
}
