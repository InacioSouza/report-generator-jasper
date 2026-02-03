package br.com.report_generator.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name="sistema")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Sistema {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String nome;
    private String descricao;

    @Version
    private Integer versao;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "sistema")
    List<Relatorio> relatorios;
}
