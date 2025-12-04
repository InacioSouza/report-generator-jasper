package br.com.report_generator.model.dto.template;

import br.com.report_generator.model.dto.sistema.SistemaDto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record TemplateUploadDto(
        @NotNull
        String titulo,
        String subtitulo,
        String nomeTemplate,
        String informacaoRelatorio,
        String descricaoTemplate,
        @NotNull
        @NotEmpty
        MultipartFile arquivoOriginal,
        @NotNull
        SistemaDto sistema
) {
}
