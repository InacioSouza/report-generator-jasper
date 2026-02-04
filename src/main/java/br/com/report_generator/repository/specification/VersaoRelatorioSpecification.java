package br.com.report_generator.repository.specification;

import br.com.report_generator.dto.filtros.VersaoRelatorioFiltroDto;
import br.com.report_generator.model.VersaoRelatorio;
import br.com.report_generator.service.utils.SecurityUtil;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class VersaoRelatorioSpecification {

    public static Specification<VersaoRelatorio> filtro(VersaoRelatorioFiltroDto filtroDto) {

        return (root, query, cb) -> {

            List<Predicate> listPredicate = new ArrayList<>();

            if (SecurityUtil.usuarioAutenticaEhAdmin() && filtroDto.idSistema() != null) {
                listPredicate.add(
                        cb.equal(
                                root.get("relatorio")
                                        .get("sistema")
                                        .get("id")
                                , filtroDto.idSistema()
                        )
                );

            } else {
                listPredicate.add(
                        cb.equal(
                                root.get("relatorio")
                                        .get("sistema")
                                        .get("id")
                                , SecurityUtil.buscaIdSistemaAutenticado()
                        )
                );
            }

            if (filtroDto.id() != null) {
                listPredicate.add(
                        cb.equal(root.get("id"), filtroDto.id())
                );
            }

            if (filtroDto.nome() != null) {
                listPredicate.add(
                        cb.like(root.get("nome"), "%" + filtroDto.nome() + "%")
                );
            }

            if (filtroDto.nomeArquivo() != null) {
                listPredicate.add(
                        cb.like(root.get("nomeArquivo"), "%" + filtroDto.nomeArquivo() + "%")
                );
            }

            if (filtroDto.idRelatorio() != null) {
                listPredicate.add(
                        cb.equal(root.get("relatorio").get("id"), filtroDto.idRelatorio())
                );
            }

            if (filtroDto.numeroVersao() != null) {
                listPredicate.add(
                        cb.equal(root.get("numeroVersao"), filtroDto.numeroVersao())
                );
            }

            if (filtroDto.descricaoVersao() != null) {
                listPredicate.add(
                        cb.like(root.get("descricaoVersao"), filtroDto.descricaoVersao())
                );
            }

            // Filtros de data
            if (filtroDto.dataCriacaoInicio() != null ){

                if (filtroDto.dataCriacaoFim() != null) {
                    listPredicate.add(
                            cb.between(
                                    root.get("dataCriacao"),
                                    filtroDto.dataCriacaoInicio(),
                                    filtroDto.dataCriacaoFim()
                            )
                    );

                } else {
                    listPredicate.add(
                            cb.greaterThanOrEqualTo(
                                    root.get("dataCriacao"),
                                    filtroDto.dataCriacaoInicio())
                    );
                }


            }  else if (filtroDto.dataCriacaoFim() != null) {
                listPredicate.add(
                        cb.lessThanOrEqualTo(
                                root.get("dataCriacao"),
                                filtroDto.dataCriacaoFim()
                        )
                );
            }

            if (filtroDto.ultimaAtualizacao() != null) {
                listPredicate.add(
                        cb.equal(root.get("ultimaAtualizacao"), filtroDto.ultimaAtualizacao())
                );
            }

            return cb.and(listPredicate.toArray(new Predicate[0]));
        };
    }
}
