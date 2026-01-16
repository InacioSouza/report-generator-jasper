package br.com.report_generator.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "relatorio")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Relatorio {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "titulo_padrao")
    private String tituloPadrao;

    @Column(name = "subtitulo_padrao")
    private String subtituloPadrao;

    @Column(name = "nome")
    private String nome;

    @Column(name = "informacao")
    private String informacao;

    @Column(name = "descricao_tecnica")
    private String descricaoTecnica;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sistema_id")
    private Sistema sistema;

    @Column(name = "data_criacao")
    private Date dataCriacao;

    @Column(name = "numero_ultima_versao")
    private Integer numeroUltimaVersao;

    @Version
    private Integer versao;

    @OneToMany(mappedBy = "relatorio",
            cascade = CascadeType.PERSIST,
            orphanRemoval = true)
    private List<VersaoRelatorio> listVersoes;
}
