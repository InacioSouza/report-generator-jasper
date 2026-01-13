package br.com.report_generator.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum ExibicaoRelatorio {
    INLINE("inline"),
    ATTACHMENT("attachment");

    private String exibicao;

    ExibicaoRelatorio(String exibicao) {
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
