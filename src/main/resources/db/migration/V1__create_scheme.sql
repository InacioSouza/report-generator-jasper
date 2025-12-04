CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE sistema (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome VARCHAR(350) NOT NULL,
    descricao VARCHAR(450)
);

CREATE TABLE template (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    titulo VARCHAR(350) NOT NULL,
    subtitulo VARCHAR(350),
    nome_arquivo VARCHAR(350),
    nome_template VARCHAR(350),
    informacao_relatorio VARCHAR(500),
    descricao_template VARCHAR(500),
    tipo_arquivo VARCHAR(10) NOT NULL,
    arquivo_compilado BYTEA NOT NULL,
    arquivo_original BYTEA,
    sistema BIGINT NOT NULL,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (sistema) REFERENCES sistema(id)
);