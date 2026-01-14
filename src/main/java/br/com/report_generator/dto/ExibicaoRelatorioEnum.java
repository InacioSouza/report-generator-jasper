package br.com.report_generator.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum ExibicaoRelatorioEnum {
    INLINE("inline"),
    ATTACHMENT("attachment");

    private String exibicao;

    ExibicaoRelatorioEnum(String exibicao) {
        this.exibicao = exibicao;
    }

    @Override
    public String toString() {
        return this.exibicao;
    }

    @JsonValue
    public String jsonValue() {
        return this.name();
    }
}
