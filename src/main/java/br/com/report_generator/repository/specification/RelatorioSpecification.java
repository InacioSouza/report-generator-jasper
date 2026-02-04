package br.com.report_generator.repository.specification;

import br.com.report_generator.dto.filtros.RelatorioFiltroDto;
import br.com.report_generator.model.Relatorio;
import br.com.report_generator.service.utils.SecurityUtil;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RelatorioSpecification {
    public static Specification<Relatorio> filtro(RelatorioFiltroDto filtroDto) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (SecurityUtil.usuarioAutenticaEhAdmin() && filtroDto.idSistema() != null) {
                predicates.add(
                        cb.equal(root.get("sistema").get("id"), filtroDto.idSistema())
                );

            } else {
                // Um usuário (sistema) não ADMIN deve acessar somente as informações que pertencem a ele
                UUID idSistemaAutenticado = SecurityUtil.buscaIdSistemaAutenticado();

                predicates.add(
                        cb.equal(root.get("sistema").get("id"), idSistemaAutenticado)
                );
            }

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

            if (filtroDto.numeroUltimaVersao() != null) {
                predicates.add(
                        cb.equal(root.get("numeroUltimaVersao"), filtroDto.numeroUltimaVersao())
                );
            }

            // Filtros de data
            if (filtroDto.dataCriacaoInicio() != null ){

                if (filtroDto.dataCriacaoFim() != null) {
                    predicates.add(
                            cb.between(
                                    root.get("dataCriacao"),
                                    filtroDto.dataCriacaoInicio(),
                                    filtroDto.dataCriacaoFim()
                            )
                    );

                } else {
                    predicates.add(
                            cb.greaterThanOrEqualTo(
                                    root.get("dataCriacao"),
                                    filtroDto.dataCriacaoInicio())
                    );
                }


            }  else if (filtroDto.dataCriacaoFim() != null) {
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
