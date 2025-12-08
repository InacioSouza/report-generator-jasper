package br.com.report_generator.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "arquivo_subreport")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ArquivoSubreport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "versao_relatorio_id")
    private UUID versaoRelatorio;
    private byte[] arquivoCompilado;
    private byte[] arquivoOriginal;
}
