package br.com.report_generator.repository;

import br.com.report_generator.model.VersaoRelatorio;
import br.com.report_generator.repository.generic.GenericRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VersaoRelatorioRepository extends GenericRepository<VersaoRelatorio, UUID> {

    @Query(
            value = """
                    SELECT DISTINCT v 
                    FROM VersaoRelatorio v 
                    WHERE v.relatorio.id = :idRelatorio AND v.numeroVersao = :numeroVersao
                    """
    )
    Optional<VersaoRelatorio> buscaVersaoRelatorioPorIdRelatorio(UUID idRelatorio, Integer numeroVersao);

    @Query(
            value = """
                    SELECT DISTINCT v 
                    FROM VersaoRelatorio v
                    WHERE v.relatorio.id = :idRelatorio
                    ORDER BY v.dataCriacao DESC
                    """
    )
    Optional<VersaoRelatorio> buscaVersaoRelatorioMaisRecentePara(UUID idRelatorio);
}
