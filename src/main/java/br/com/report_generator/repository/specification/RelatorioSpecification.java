package br.com.report_generator.repository.specification;

import br.com.report_generator.dto.filtros.RelatorioFiltroDto;
import br.com.report_generator.model.Relatorio;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class RelatorioSpecification {
    public static Specification<Relatorio> filtro(RelatorioFiltroDto filtroDto) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filtroDto.idRelatorio() != null) {
                predicates.add(
                        cb.equal(root.get("id"), filtroDto.idRelatorio())
                );
            }

            if (filtroDto.nomeRelatorio() != null) {
                String stringTratada = filtroDto.nomeRelatorio().replace(" ", "");
                predicates.add(
                        cb.like(root.get("nome"), "%" + stringTratada + "%")
                );
            }

            if (filtroDto.tituloPadrao() != null) {
                String stringTratada = filtroDto.tituloPadrao().replace(" ", "");
                predicates.add(
                        cb.like(root.get("tituloPadrao"), "%" + stringTratada + "%")
                );
            }

            if (filtroDto.subtituloPadrao() != null) {
                String stringTratada = filtroDto.subtituloPadrao().replace(" ", "");
                predicates.add(
                        cb.like(root.get("subtituloPadrao"), "%" + stringTratada + "%")
                );
            }

            if (filtroDto.informacao() != null) {
                String stringTratada = filtroDto.informacao().replace(" ", "");
                predicates.add(
                        cb.like(root.get("informacao"), "%" + stringTratada + "%")
                );
            }

            if (filtroDto.descricaoTecnica() != null) {
                String stringTratada = filtroDto.descricaoTecnica().replace(" ", "");
                predicates.add(
                        cb.like(root.get("descricaoTecnica"), "%" + stringTratada + "%")
                );
            }

            if (filtroDto.idSistema() != null) {
                predicates.add(
                  cb.equal(root.get("sistema").get("id"), filtroDto.idSistema())
                );
            }

            if (filtroDto.numeroUltimaVersao() != null) {
                predicates.add(
                        cb.equal(root.get("numeroUltimaVersao"), filtroDto.numeroUltimaVersao())
                );
            }

            // Filtros de data
            if (filtroDto.dataCriacaoInicio() != null &&
                    filtroDto.dataCriacaoFim() != null) {

                predicates.add(
                        cb.between(
                                root.get("dataCriacao"),
                                filtroDto.dataCriacaoInicio(),
                                filtroDto.dataCriacaoFim()
                        )
                );

            } else if (filtroDto.dataCriacaoInicio() != null) {
                predicates.add(
                        cb.greaterThanOrEqualTo(
                                root.get("dataCriacao"),
                                filtroDto.dataCriacaoInicio())
                );

            } else if (filtroDto.dataCriacaoFim() != null) {
                predicates.add(
                  cb.lessThanOrEqualTo(
                          root.get("dataCriacao"),
                          filtroDto.dataCriacaoFim()
                  )
                );
            }


            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
