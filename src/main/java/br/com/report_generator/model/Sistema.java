package br.com.report_generator.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name="sistema")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Sistema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String descricao;
    private Integer versao;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "sistema")
    List<Relatorio> relatorios;
}
