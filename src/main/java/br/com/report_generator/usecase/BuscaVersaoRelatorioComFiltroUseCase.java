package br.com.report_generator.usecase;

import br.com.report_generator.dto.filtros.VersaoRelatorioFiltroDto;
import br.com.report_generator.dto.versaorelatorio.VersaoRelatorioResponseDto;
import br.com.report_generator.model.VersaoRelatorio;
import br.com.report_generator.repository.VersaoRelatorioRepository;
import br.com.report_generator.repository.specification.VersaoRelatorioSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BuscaVersaoRelatorioComFiltroUseCase {

    private final VersaoRelatorioRepository versaoRelatorioRepository;

    public BuscaVersaoRelatorioComFiltroUseCase(VersaoRelatorioRepository versaoRelatorioRepository) {
        this.versaoRelatorioRepository = versaoRelatorioRepository;
    }

    public List<VersaoRelatorioResponseDto> executar(VersaoRelatorioFiltroDto filtroDto) {

        Specification<VersaoRelatorio> versaoRelatorioSpecification = VersaoRelatorioSpecification
                .filtro(filtroDto);

        List<VersaoRelatorioResponseDto> listDTO = this.versaoRelatorioRepository
                .findAll(versaoRelatorioSpecification)
                .stream()
                .map(VersaoRelatorioResponseDto::new)
                .toList();

        return listDTO;
    }

}
