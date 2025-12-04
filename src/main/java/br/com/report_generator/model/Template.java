package br.com.report_generator.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "templates")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Template {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String titulo;

    private String subtitulo;

    @Column(name = "nome_arquivo")
    private String nomeArquivo;

    @Column(name = "nome_template")
    private String nomeTemplate;

    @Column(name = "informacao_relatorio")
    private String informacaoRelatorio;

    @Column(name = "descricao_template")
    private String descricaoTemplate;

    @Enumerated(EnumType.STRING)
    private TipoArquivoEnum tipoArquivo;

    @Lob
    @Column(name = "arquivo_digital")
    private byte[] arquivoCompilado;

    @Lob
    @Column(name = "arquivo_original")
    private byte[] arquivoOriginal;

    @ManyToOne(fetch = FetchType.LAZY)
    private Sistema sistema;

    @Column(name = "data_criacao")
    private LocalDate dataCriacao;

    private Integer versao;
}
