package br.com.report_generator.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/*
 Usado para definir a palavra que identifica o arquivo principal em um relatório com múltiplos arquivos
*/

@Getter
public enum IdentificadorArquivoPrincipalEnum {
    MAIN("MAIN");

    private String identificador;

    IdentificadorArquivoPrincipalEnum(String descricao) {
        this.identificador = descricao;
    }

    @Override
    public String toString() {
        return this.identificador;
    }

    @JsonValue
    public String jsonValue() {
        return this.name();
    }
}
